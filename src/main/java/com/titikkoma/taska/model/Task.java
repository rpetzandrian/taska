package com.titikkoma.taska.model;

import com.titikkoma.taska.base.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Task implements BaseEntity<String> {
    private String id;
    private String name;
    private String sprint_id;
    private String description;
    private String status;
    private Integer priority;
    private Integer story_point;
    private String reporter_id;
    private String assignee_id;
    private String type;

    public Task(
            String id,
            String name,
            String sprint_id,
            String description,
            String status,
            Integer priority,
            Integer story_point,
            String reporter_id,
            String assignee_id,
            String type
    ) {
        if (id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }

        this.name = name;
        this.description = description;
        this.sprint_id = sprint_id;
        this.status = status;
        this.priority = priority;
        this.story_point = story_point;
        this.reporter_id = reporter_id;
        this.assignee_id = assignee_id;
        this.type = type;
    }

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

    public Integer getStory_point() {
        return story_point;
    }

    public void setStory_point(Integer story_point) {
        this.story_point = story_point;
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

    public String getSprint_id() {
        return sprint_id;
    }

    public void setSprint_id(String sprint_id) {
        this.sprint_id = sprint_id;
    }

    @Override
    public Map<String, Object> toInsertMap() {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        map.put("id", id);

        if (name != null) map.put("name", name);
        if (description != null) map.put("description", description);
        if (status != null) map.put("status", status);
        if (priority != null) map.put("priority", priority);
        if (story_point != null) map.put("story_point", story_point);
        if (reporter_id != null) map.put("reporter_id", reporter_id);
        if (assignee_id != null) map.put("assignee_id", assignee_id);
        if (type != null) map.put("type", type);
        if (sprint_id != null) map.put("sprint_id", sprint_id);

        map.replaceAll((key, value) -> "".equals(value) ? null : value);

        return map;
    }
}
