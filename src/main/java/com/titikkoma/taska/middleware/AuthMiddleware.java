package com.titikkoma.taska.middleware;

import com.titikkoma.taska.entity.User;
import com.titikkoma.taska.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthMiddleware implements HandlerInterceptor {

    private UserRepository userRepository;
    public AuthMiddleware(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ResponseStatusException {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        System.out.println(token);

        Map<String, Object> cond = new HashMap<>();
        cond.put("token", token);
        User user = userRepository.findOne(cond)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        if (!user.getToken().equals(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Instant now = Instant.now();
        if (user.getExpired_at().toInstant().toEpochMilli() < now.toEpochMilli()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        return true;  // Allow the request to proceed
    }
}
