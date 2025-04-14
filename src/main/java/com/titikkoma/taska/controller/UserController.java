package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.entity.User;
import com.titikkoma.taska.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        System.out.println("test");
        Optional<User> user =  userService.findById();
        return WebResponse.<Optional<User>>builder().data(user).build();
    }

    @GetMapping("/v1/users")
    public WebResponse<List<User>> findAll() {
        System.out.println("list");
        List<User> users = userService.findAllUsers();
        return WebResponse.<List<User>>builder().data(users).build();
    }
}
