package com.titikkoma.taska.service;

import com.titikkoma.taska.entity.User;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository =  userRepository;
    }

    public Optional<User> findById() {
        return userRepository.findByEmail("testing-email");
    }

    public List<User> findAllUsers() {
        Map<String, Object> userCond = new HashMap<>();
        return userRepository.findAll(userCond);
    }
}
