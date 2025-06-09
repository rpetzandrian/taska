package com.titikkoma.taska.service;

import com.titikkoma.taska.dto.UserListResponse;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

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

    public List<UserListResponse> findAllUsers() {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> userCond = new HashMap<>();
        userCond.put("organization_code", principal.getOrganizationCode());
        List<User> users = userRepository.findAll(userCond);

        // create builder
        List<UserListResponse> userListResponses = new ArrayList<>();
        for (User user : users) {
            UserListResponse userListResponse = UserListResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .organization_code(user.getOrganization_code())
                    .build();

            userListResponses.add(userListResponse);
        }

        return userListResponses;
    }
}
