package nl.lpp.uncenshow

import nl.lpp.uncenshow.interceptor.TokenInterceptor
import nl.lpp.uncenshow.service.authentication.AuthenticationService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
class WebMvcConfig (private val authenticationService: AuthenticationService) : WebMvcConfigurer {
    // override fun addInterceptors(registry: InterceptorRegistry) {
    //     // registry.addInterceptor(TokenInterceptor(authenticationService))
    // }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:5173")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true)
            }
        }
    }
}