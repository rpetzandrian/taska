package com.titikkoma.taska.entity;

import com.titikkoma.taska.base.BaseEntity;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class User implements BaseEntity<String> {
    private String id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String organization_code;
    private String token;
    private Timestamp expired_at;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setOrganization_code(String organization_code) {
        this.organization_code = organization_code;
    }

    public String getOrganization_code() {
        return organization_code;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setExpired_at(Timestamp expired_at) {
        this.expired_at = expired_at;
    }

    public Timestamp getExpired_at() {
        return expired_at;
    }

    public Map<String, Object> toInsertMap() {
        Map<String, Object> map = new HashMap<>();

        User user = this;
        map.put("id", user.getId());
        if (user.getName() != null) map.put("name", user.getName());
        if (user.getEmail() != null) map.put("email", user.getEmail());
        if (user.getPassword() != null) map.put("password", user.getPassword());
        if (user.getRole() != null) map.put("role", user.getRole());
        if (user.getOrganization_code() != null) map.put("organization_code", user.getOrganization_code());
        if (user.getToken() != null) map.put("token", user.getToken());
        if (user.getExpired_at() != null) map.put("expired_at", user.getExpired_at());

        return map;
    }
}
