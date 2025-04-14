package com.titikkoma.taska.entity;

import com.titikkoma.taska.base.BaseEntity;

import java.util.Map;

public class Task implements BaseEntity<String> {
    private String id;
    private String name;
    private String description;
    private String status;
    private Integer priority;
    private Integer story_points;
    private String reporter_id;
    private String assignee_id;
    private String type;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String s) {
        this.id = s;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStory_points() {
        return story_points;
    }

    public void setStory_points(Integer story_points) {
        this.story_points = story_points;
    }

    public String getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(String reporter_id) {
        this.reporter_id = reporter_id;
    }

    public String getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(String assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Map<String, Object> toInsertMap() {
        return Map.of();
    }
}
