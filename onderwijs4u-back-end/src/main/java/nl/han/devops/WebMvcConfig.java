package nl.han.devops;

import nl.han.devops.interceptor.TokenInterceptor;
import nl.han.devops.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final AuthenticationService authenticationService;

    @Autowired
    public WebMvcConfig(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

     @Override
     public void addInterceptors(InterceptorRegistry registry) {
         registry.addInterceptor(new TokenInterceptor(authenticationService));
     }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

