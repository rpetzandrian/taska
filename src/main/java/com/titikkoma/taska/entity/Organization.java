package com.titikkoma.taska.entity;

import com.titikkoma.taska.base.BaseEntity;

import java.util.Map;

public class Organization implements BaseEntity<String> {
    private String id;
    private String name;
    private String description;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Map<String, Object> toInsertMap() {
        return Map.of();
    }
}
