package linepaytest.LinePayDemo.Security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import linepaytest.LinePayDemo.Dao.Oauth2MemberDao;
import linepaytest.LinePayDemo.Model.Oauth2Member;

@Component
public class MyJwtAuthenticaticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private MyJwtUtil myJwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private Oauth2MemberDao oauth2MemberDao;

    // doFilterInternal 方法會在每次 HTTP 請求進來時執行
    // 這個方法會檢查 HTTP 請求中是否有 JWT 字串
    // 如果有 JWT 字串，就會解析 JWT 字串，並且驗證 JWT 字串是否有效
    // 如果 JWT 字串有效，就會取出 JWT 字串中的 email，並且透過 email 取得使用者的資訊
    // 最後，將使用者的資訊存入 SecurityContextHolder 中
    // 這樣，Spring Security 就會知道這個使用者是誰，並且可以進行授權
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                // 取得 HTTP 請求的 URI 路徑
                // 如果 URI 路徑是 /register 或 /login 就不用進行驗證，直接放行
                String requestURI = request.getRequestURI();
                if (requestURI.equals("/user_register") || requestURI.equals("/user_login")) {
                    filterChain.doFilter(request, response);
                    return;
                }

                // 解析 JWT 
                String token = myJwtUtil.extractToken(request);
                if (token != null && myJwtUtil.validateToken(token)) {
                    String email = myJwtUtil.getEmailFromToken(token);
                    String authType = myJwtUtil.getAuthTypeFromToken(token); // 取得 authType

                    if ("JWT".equals(authType)) {
                        // 透過 JWT 的方式驗證使用者
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else if ("OAuth2".equals(authType)) {
                        // 透過 OAuth2 的方式驗證使用者
                        Oauth2Member oauth2Member = oauth2MemberDao.getOauth2MemberByEmail(email);
                        if (oauth2Member != null) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                oauth2Member, null, null); // OAuth2 沒有 authorities
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
                filterChain.doFilter(request, response);
            }
}
