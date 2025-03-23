package linepaytest.LinePayDemo.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.http.SessionCreationPolicy;

import java.util.Arrays;


@Configuration
public class MySecurityConfig {
    
    @Autowired
    private MyJwtAuthenticaticationFilter myJwtAuthenticaticationFilter;
    
    @Autowired
    private MyUserDetailService myUserDetailsService;

    @Autowired
    private MyOidcUserService myOidcUserService;

    @Autowired
    private MyOauth2JwtSuccessHandler myOauth2JwtSuccessHandler;

    // 設定密碼加密器
    // 這裡使用 BCryptPasswordEncoder 來加密密碼
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    // 設定 AuthenticationManager
    // AuthenticationManager 是 Spring Security 的核心
    // 它負責驗證使用者的身份
    // 這裡我們使用 DaoAuthenticationProvider 來驗證使用者的身份
    // DaoAuthenticationProvider 需要設定 UserDetailsService 和 PasswordEncoder
    // UserDetailsService 是用來取得使用者的資訊
    // PasswordEncoder 是用來驗證使用者的密碼
    // 最後，我們將 DaoAuthenticationProvider 放入 ProviderManager 中
    // 這樣，我們就建立了一個 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    // 設定 SecurityFilterChain
    // SecurityFilterChain 是 Spring Security 的核心
    // 它負責過濾 HTTP 請求，並且進行授權
    // 這裡我們設定了幾個 HTTP 請求的規則
    // 1. /register 和 /login 可以不用驗證就可以存取
    // 2. /profile 必須要驗證才能存取
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf().disable()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)) // 使用session當oauth2需要時才建立 
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/register","/user_register", "/user_register.html", "/login", "/user_login", "/user_login.html",
                                    "/user_profile", "/user_profile.html", "shop_page", "/shop_page.html", 
                                    "/linepay_pay", "/linepay_pay.html", "/redirect", "/order_success", "/order_success.html").permitAll() 
                .requestMatchers("/profile").authenticated() // 這裡的 user_profile 接口
                .requestMatchers("/products", "/cart/add", "/cart/items", "/cart/clear").authenticated() // 這裡的 shop_page 接口
                .requestMatchers("/request").authenticated() // 這裡的 linepay_pay 接口
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/oauth2/authorization/google")
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(myOidcUserService))
                .successHandler(myOauth2JwtSuccessHandler)) // 登入成功後，產生 JWT token
            .formLogin(login -> login.disable())
            .logout(logout -> logout.disable())
            .addFilterBefore(myJwtAuthenticaticationFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    // 設定跨域請求
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // 允許來自這些來源的跨域請求
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://127.0.0.1:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 如有需要
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
