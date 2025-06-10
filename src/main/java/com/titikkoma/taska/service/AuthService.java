package com.titikkoma.taska.service;

import com.titikkoma.taska.base.helpers.BCrypt;
import com.titikkoma.taska.dto.LoginResponse;
import com.titikkoma.taska.dto.RegisterRequestBody;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.OrganizationRepository;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService{
    private UserRepository userRepository;
    private OrganizationRepository organizationRepository;

    public AuthService(
        UserRepository userRepository,
        OrganizationRepository organizationRepository
    ) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public LoginResponse login(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

        String pw = user.get().getPassword();
        boolean isPasswordCorrect = BCrypt.checkpw(password, pw);

        if (!isPasswordCorrect) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        }

        Timestamp expiredAt = new Timestamp(Instant.now().plus(3600000, ChronoUnit.SECONDS).toEpochMilli());

        String token = UUID.randomUUID().toString();
        User updateUserPayload = new User();
        updateUserPayload.setToken(token);
        updateUserPayload.setExpired_at(expiredAt);

        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", user.get().getId());
        userRepository.update(conditions, updateUserPayload.toInsertMap());

        return LoginResponse.builder()
                .token(token)
                .expired_at(expiredAt.toString())
                .role(user.get().getRole())
                .lifetime(3600000)
                .build();
    }

    public LoginResponse register(RegisterRequestBody registerRequestBody) {
        Map<String, Object> orgCond = new HashMap<>();
        orgCond.put("code", registerRequestBody.getOrganization_code());
        organizationRepository.findOneOrFail(orgCond);

        Optional<User> existing = userRepository.findByEmail(registerRequestBody.getEmail());

        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in registered");
        }

        String hashedPassword = BCrypt.hashpw(registerRequestBody.getPassword(), BCrypt.gensalt());

        String token = UUID.randomUUID().toString();
        Timestamp expiredAt = new Timestamp(Instant.now().plus(3600000, ChronoUnit.SECONDS).toEpochMilli());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(registerRequestBody.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("member");
        user.setName(registerRequestBody.getName());
        user.setOrganization_code(registerRequestBody.getOrganization_code());
        user.setToken(token);
        user.setExpired_at(expiredAt);

        userRepository.create(user);

        return LoginResponse.builder()
                .token(token)
                .expired_at(expiredAt.toString())
                .role("member")
                .lifetime(3600000)
                .build();
    }

    public LoginResponse registerAdmin(RegisterRequestBody registerRequestBody) {

        Optional<User> existing = userRepository.findByEmail(registerRequestBody.getEmail());

        if (existing.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in registered");
        }

        String hashedPassword = BCrypt.hashpw(registerRequestBody.getPassword(), BCrypt.gensalt());

        String token = UUID.randomUUID().toString();
        Timestamp expiredAt = new Timestamp(Instant.now().plus(3600000, ChronoUnit.SECONDS).toEpochMilli());

        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setEmail(registerRequestBody.getEmail());
        user.setPassword(hashedPassword);
        user.setRole("admin");
        user.setName(registerRequestBody.getName());
        user.setOrganization_code(registerRequestBody.getOrganization_code());
        user.setToken(token);
        user.setExpired_at(expiredAt);

        userRepository.create(user);

        return LoginResponse.builder()
                .token(token)
                .expired_at(expiredAt.toString())
                .role("admin")
                .lifetime(3600000)
                .build();
    }
}
