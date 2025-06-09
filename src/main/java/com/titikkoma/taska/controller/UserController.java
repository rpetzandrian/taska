package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.UserListResponse;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/v1/users/test")
    public WebResponse<Optional<User>> findById() {
        Optional<User> user =  userService.findById();
        return WebResponse.<Optional<User>>builder().data(user).build();
    }

    @GetMapping("/v1/users")
    public WebResponse<List<UserListResponse>> findAll() {
        List<UserListResponse> users = userService.findAllUsers();
        return WebResponse.<List<UserListResponse>>builder().data(users).build();
    }
}
