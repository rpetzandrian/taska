package com.titikkoma.taska;

import com.titikkoma.taska.middleware.AuthMiddleware;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class Config implements WebMvcConfigurer {
    private AuthMiddleware authMiddleware;

    public Config(AuthMiddleware authMiddleware) {
        this.authMiddleware = authMiddleware;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authMiddleware)
                .excludePathPatterns("/v1/auth/*");
    }
}