package com.titikkoma.taska.entity;

import com.titikkoma.taska.base.BaseEntity;

public class User implements BaseEntity<String> {
    private String id;
    private String name;
    private String email;

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
}
