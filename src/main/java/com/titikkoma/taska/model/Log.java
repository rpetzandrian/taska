package com.titikkoma.taska.model;

import com.titikkoma.taska.base.BaseEntity;

import java.util.Map;

public class Log implements BaseEntity<String> {
    private String id;
    private String action;
    private String date;
    private String type;
    private String reference_id;
    private Object content;

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

    public String getReference_id() { return reference_id; }

    public void setReference_id(String reference_id) { this.reference_id = reference_id; }

    public Object getContent() { return content; }

    public void setContent(Object content) { this.content = content; }

    @Override
    public Map<String, Object> toInsertMap() {
        return Map.of();
    }
}
