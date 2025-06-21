package com.titikkoma.taska.service;

import com.titikkoma.taska.dto.UserResponse;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(
            UserRepository userRepository
    ) {
        this.userRepository =  userRepository;
    }

    public UserResponse findById(String id) {
        Map<String, Object> cond = new HashMap<>();
        cond.put("id", id);

        User user = userRepository.findOneOrFail(cond);

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .organization_code(user.getOrganization_code())
                .build();
    }

    public List<UserResponse> findAllUsers() {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> userCond = new HashMap<>();
        userCond.put("organization_code", principal.getOrganizationCode());
        List<User> users = userRepository.findAll(userCond);

        // create builder
        List<UserResponse> userListResponses = new ArrayList<>();
        for (User user : users) {
            UserResponse userListResponse = UserResponse.builder()
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
