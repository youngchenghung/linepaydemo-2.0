package linepaytest.LinePayDemo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class MyOauth2JwtSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private MyJwtUtil myJwtUtil;

    // 這個方法會在Oauth2使用者登入成功後被呼叫
    // 我們可以在這裡取得使用者的資訊，並產生一個 JWT token
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                String email = oidcUser.getAttribute("email");

                String token = myJwtUtil.generateToken(email, "OAuth2");

                response.setHeader("Authorization", "Bearer " + token);

                response.sendRedirect("/user_profile?token=" + token);
            }
}
