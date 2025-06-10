package com.titikkoma.taska.model;

import com.titikkoma.taska.base.BaseEntity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Sprint implements BaseEntity<String> {
    private String id;
    private String name;
    private String description;
    private Timestamp start_date;
    private Timestamp end_date;
    private String status;
    private String organization_code;
    private String created_by;

    public Sprint(
            String id,
            String name,
            String description,
            Timestamp start_date,
            Timestamp end_date,
            String status,
            String organization_code,
            String created_by
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
        this.organization_code = organization_code;
        this.created_by = created_by;
    }

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

    public Timestamp getStart_date() {
        return start_date;
    }

    public Timestamp getEnd_date() {
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

    public void setStart_date(Timestamp start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(Timestamp end_date) {
        this.end_date = end_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganization_code() { return organization_code; }

    public void setOrganization_code(String organization_code) { this.organization_code = organization_code; }

    public String getCreated_by() { return created_by; }

    public void setCreated_by(String created_by) { this.created_by = created_by; }

    @Override
    public Map<String, Object> toInsertMap() {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        map.put("id", id);

        if (name != null) map.put("name", name);
        if (description != null) map.put("description", description);
        if (start_date != null) map.put("start_date", start_date);
        if (end_date != null) map.put("end_date", end_date);
        if (status != null) map.put("status", status);
        if (organization_code != null) map.put("organization_code", organization_code);
        if (created_by != null) map.put("created_by", created_by);

        map.replaceAll((key, value) -> "".equals(value) ? null : value);
        return map;
    }
}
