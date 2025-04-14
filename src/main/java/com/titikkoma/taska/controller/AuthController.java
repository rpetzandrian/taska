package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.LoginRequestBody;
import com.titikkoma.taska.dto.LoginResponse;
import com.titikkoma.taska.dto.RegisterRequestBody;
import com.titikkoma.taska.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(
            path = "/v1/auth/login"
    )
    public WebResponse<LoginResponse> login(@RequestBody LoginRequestBody request) {
        LoginResponse resp = authService.login(request.getEmail(), request.getPassword());
        return WebResponse.<LoginResponse>builder().data(resp).build();
    }

    @PostMapping(
            path = "/v1/auth/register"
    )
    public WebResponse<LoginResponse> register(@RequestBody RegisterRequestBody request) {
        LoginResponse resp = authService.register(request);
        return WebResponse.<LoginResponse>builder().data(resp).build();
    }
}
