package linepaytest.LinePayDemo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import linepaytest.LinePayDemo.Dao.MemberDao;
import linepaytest.LinePayDemo.Dao.Oauth2MemberDao;
import linepaytest.LinePayDemo.Model.Member;
import linepaytest.LinePayDemo.Model.Oauth2Member;
import linepaytest.LinePayDemo.Security.MyJwtUtil;

import java.util.Map;
import java.util.logging.Logger;


@RestController
public class MemberController {
    
    private static final Logger logger = Logger.getLogger(MemberController.class.getName());

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;
    private final MyJwtUtil myJwtUtil;
    private final AuthenticationManager authenticationManager;

    public MemberController(
                            MemberDao memberDao,
                            PasswordEncoder passwordEncoder,
                            MyJwtUtil myJwtUtil,
                            AuthenticationManager authenticationManager
                            ){
        this.memberDao = memberDao;
        this.passwordEncoder = passwordEncoder;
        this.myJwtUtil = myJwtUtil;
        this.authenticationManager = authenticationManager;
    }
                            

    @Autowired
    private Oauth2MemberDao oauth2MemberDao;

    // 註冊會員帳號
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member) {
        // 檢查是否有缺少必要欄位
        System.out.println(member.getMemberName());
        if (member.getMemberName() == null || member.getMemberName().isBlank() || 
            member.getEmail() == null || member.getEmail().isBlank() || 
            member.getPassword() == null || member.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing required fields"));
        }
        
        // 檢查是否已經註冊過
        Integer memberIdCheck = memberDao.getMemberIdByEmail(member.getEmail());
        if(memberIdCheck != null) {
            logger.warning("Email already exists: " + member.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already exists"));
        }

        // 將密碼加密
        String hashedPassword = passwordEncoder.encode(member.getPassword());
        // 將加密後的密碼設定回 member 物件
        member.setPassword(hashedPassword);
        
        // 會員未被注冊，新增註冊會員
        memberDao.register(member);
        logger.info("New member registered: " + member.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("Email: " + member.getEmail() , "Created member successfully"));
    }

    // 登入會員帳號
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Member member){
        try {
            // 會員資料傳入 authenticationManager，進行資料庫比對
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword()));
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String email = userDetails.getUsername();
            // 生成 JWT token
            String token = myJwtUtil.generateToken(email, "JWT");

            Member loggedInMember = memberDao.getMemberByEmail(email);
            logger.info("User logged in : " + loggedInMember);
            logger.info("User : " + email + ", JWT token :" + token);

            return ResponseEntity.ok(Map.of("token", token));
        }
        catch (Exception e) {
            logger.warning("Invalid credentials: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid credentials"));
        }
    }

    // 取得會員資料 
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        
        // 檢查Authorization 是否存在 ”Bearer" 開頭
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token is missing"));
        }

        // 提取 token，去掉 "Bearer " 前綴（前 7 個字符）
        String token = authHeader.substring(7);
        String email;
        String authType;

        try {
            // 從 token 中取得 email 和 authType
            email = myJwtUtil.getEmailFromToken(token);
            authType = myJwtUtil.getAuthTypeFromToken(token);
        } catch (Exception e) {
            logger.warning("Token validation failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid token"));
        }

        // 根據 authType(JWT / Oauth2) 來決定如何獲取用戶資料
        if ("JWT".equals(authType)) {
            // 如果是 JWT 認證，從 memberDao 獲取 Member 對象
            Member member = memberDao.getMemberByEmail(email);
            if (member != null) {
                return ResponseEntity.ok(Map.of("memberName", member.getMemberName(), "email", member.getEmail()));
            }
        } else if ("OAuth2".equals(authType)) {
            // 如果是 OAuth2 認證，從 oauth2MemberDao 獲取 Oauth2Member 對象
            Oauth2Member oauth2Member = oauth2MemberDao.getOauth2MemberByEmail(email);
            if (oauth2Member != null) {
                return ResponseEntity.ok(Map.of("memberName", oauth2Member.getName(), "email", oauth2Member.getEmail()));
            }
        }
        // 如果沒有找到用戶，返回 404 未找到，附帶錯誤信息
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Member not found"));
    }

    // 刪除會員
    @DeleteMapping("/profile/delete")
    public ResponseEntity<?> deleteMember(@RequestParam Integer memberId) {
        // 檢查 member_id 是否存在
        Integer member_id = memberDao.getMemberIdByMemberId(memberId);
        if (member_id == null){
            return ResponseEntity.badRequest().body(Map.of("error", "member id is missing"));
        }

        // 判斷刪除回傳值是否成功
        try{
            Boolean deleteResult = memberDao.deleteMemberById(member_id);
            if (deleteResult) {
                logger.info("Member id : " + member_id + " deleted successfully");
                return ResponseEntity.status(HttpStatus.OK).body(Map.of("memberId: " + member_id , "deleted successefully"));
            }
            else{
                logger.warning("Delete failed: Member with ID " + member_id + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("Member id : " + member_id, " not found"));
            }
        }
        catch (Exception e){
            logger.warning("Error deleting member with ID " + member_id + ", " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
        
    }
    
    // OAuth2 會員登出
    @PostMapping("/oauth2/logout")
    public ResponseEntity<?> oauth2Logout(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getRemoteUser();
        logger.info("OAuth2 Logout triggered for user: " + username);
    
        // 清除 oauth2 Session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // 清理使用者 session 登錄狀態
            logger.info("Session invalidated for user " + username);
        } else {
            logger.warning("No session found for user " + username);
        }
    
        // 狀態碼為 200，表示登出請求成功處理
        response.setStatus(HttpServletResponse.SC_OK);
        logger.info("User " + username + " logged out");
        return ResponseEntity.ok(Map.of("message", "登出成功"));
    }
}
