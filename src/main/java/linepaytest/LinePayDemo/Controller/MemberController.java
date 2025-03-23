package linepaytest.LinePayDemo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;

import linepaytest.LinePayDemo.Dao.MemberDao;
import linepaytest.LinePayDemo.Dao.Oauth2MemberDao;
import linepaytest.LinePayDemo.Model.Member;
import linepaytest.LinePayDemo.Model.Oauth2Member;
import linepaytest.LinePayDemo.Security.MyJwtUtil;


@RestController
public class MemberController {
    
    @Autowired
    private MemberDao memberDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyJwtUtil myJwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private Oauth2MemberDao oauth2MemberDao;

    // 註冊會員帳號
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Member member) {
        
        // 將密碼加密
        String hashedPassword = passwordEncoder.encode(member.getPassword());
        // 將加密後的密碼設定回 member 物件
        member.setPassword(hashedPassword);

        // 檢查是否有缺少必要欄位
        if  (member.getMemberName().isBlank() || member.getMemberName() == null || 
            member.getEmail().isBlank() || member.getEmail() == null || 
            member.getPassword().isBlank() || member.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Missing required fields"));
        }
        
        // 檢查是否已經註冊過
        Integer memberIdCheck = memberDao.getMemberIdByEmail(member.getEmail());
        if(memberIdCheck != null) {
            System.out.println("Email already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Email already exists"));
        }

        // 會員未被注冊，新增註冊會員
        else {
            Integer memberId = memberDao.register(member);
            System.out.println("Member ID: " + memberId);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("memberId: " + memberId , "Created member successe"));
        }
    }

    // 登入會員帳號
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Member member){
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword()));

            System.out.println("authhentication: " + authentication.getPrincipal());
            System.out.println("Member email: " + member.getEmail());
            String token = myJwtUtil.generateToken(member.getEmail(), "JWT");

            return ResponseEntity.ok(Map.of("token", token));
        }
        catch (Exception e) {
            // System.out.println("Invalid credentials" + e.getMessage());
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

    // OAuth2 會員登出
    @PostMapping("/oauth2/logout")
    public ResponseEntity<?> oauth2Logout(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getRemoteUser();
        System.out.println("OAuth2 Logout triggered for user: " + username);
    
        // 清除 oauth2 Session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();  // 無效化 session，清理用戶登錄狀態
            System.out.println("Session invalidated for user " + username);
        } else {
            System.out.println("No session found for user " + username);
        }
    
        // 狀態碼為 200，表示登出請求成功處理
        response.setStatus(HttpServletResponse.SC_OK);
        System.out.println("User " + username + " logged out");
        return ResponseEntity.ok(Map.of("message", "登出成功"));
    }
}
