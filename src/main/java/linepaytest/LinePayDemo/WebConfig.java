package linepaytest.LinePayDemo;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/user_login.html");
        registry.addViewController("/user_register").setViewName("forward:/user_register.html");
        registry.addViewController("/user_login").setViewName("forward:/user_login.html");
        registry.addViewController("/user_profile").setViewName("forward:/user_profile.html");
        registry.addViewController("/shop_page").setViewName("forward:/shop_page.html");
        registry.addViewController("/linepay_pay").setViewName("forward:/linepay_pay.html");
        registry.addViewController("/order_success").setViewName("forward:/order_success.html");
    }
}
