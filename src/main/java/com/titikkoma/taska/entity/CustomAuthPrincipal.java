package com.titikkoma.taska.entity;

public class CustomAuthPrincipal {
    private String id;
    private String role;
    private String name;
    private String organizationCode;

    public CustomAuthPrincipal(String id, String role, String name, String organizationCode) {
        this.id = id;
        this.role = role;
        this.name = name;
        this.organizationCode = organizationCode;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }
}
