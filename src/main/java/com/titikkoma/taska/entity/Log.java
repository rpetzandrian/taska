package com.titikkoma.taska.entity;

import com.titikkoma.taska.base.BaseEntity;

import java.util.Map;

public class Log implements BaseEntity<String> {
    private String id;
    private String action;
    private String date;
    private String type;
    private String by;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    @Override
    public Map<String, Object> toInsertMap() {
        return Map.of();
    }
}
