package com.titikkoma.taska.model;

import com.titikkoma.taska.base.BaseEntity;

import java.util.Date;
import java.util.Map;

public class Sprint implements BaseEntity<String> {
    private String id;
    private String name;
    private String description;
    private Date start_date;
    private Date end_date;
    private String status;

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStart_date() {
        return start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public Map<String, Object> toInsertMap() {
        return Map.of();
    }
}
