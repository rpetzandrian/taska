package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.UserResponse;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users/me")
    public WebResponse<UserResponse> me() {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UserResponse user =  userService.findById(principal.getId());
        return WebResponse.<UserResponse>builder().data(user).build();
    }

    @GetMapping("/v1/users/:id")
    public WebResponse<UserResponse> findById(@PathVariable String id) {
        UserResponse user =  userService.findById(id);
        return WebResponse.<UserResponse>builder().data(user).build();
    }

    @GetMapping("/v1/users")
    public WebResponse<List<UserResponse>> findAll() {
        List<UserResponse> users = userService.findAllUsers();
        return WebResponse.<List<UserResponse>>builder().data(users).build();
    }
}
