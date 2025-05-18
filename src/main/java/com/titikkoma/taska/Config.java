//package com.titikkoma.taska;
//
//import com.titikkoma.taska.middleware.AuthMiddleware;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//class Config implements WebMvcConfigurer {
//    private final AuthMiddleware authMiddleware;
//
//    public Config(AuthMiddleware authMiddleware) {
//        this.authMiddleware = authMiddleware;
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(authMiddleware)
//                .excludePathPatterns("/v1/auth/*", "/v1/logs/*", "/v1/tasks/*", "/v1/users/*");
//    }
//}

package com.titikkoma.taska;


import com.titikkoma.taska.security.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity(debug = true)
public class Config {

    @Autowired
    AuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .authorizeHttpRequests(request -> {
                    request
                            .requestMatchers(HttpMethod.POST, "/v1/auth/register").permitAll()
                            .requestMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                            .anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(authFilter, BasicAuthenticationFilter.class);
        return http.build();
    }

}