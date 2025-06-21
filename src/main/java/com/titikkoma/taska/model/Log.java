package com.titikkoma.taska.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titikkoma.taska.base.BaseEntity;
import org.springframework.jdbc.core.SqlParameterValue;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Log implements BaseEntity<String> {
    private String id;
    private String action;
    private Timestamp created_at;
    private String type;
    private String reference_id;
    private Object content;

    public Log(
            String id,
            String action,
            Timestamp created_at,
            String type,
            String reference_id,
            Object content
    ) {
        if (id == null) {
            this.id = UUID.randomUUID().toString();
        } else {
            this.id = id;
        }

        this.action = action;
        this.created_at = created_at;
        this.type = type;
        this.reference_id = reference_id;
        this.content = content;
    }

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

    public Timestamp getDate() {
        return created_at;
    }

    public void setDate(Timestamp date) {
        this.created_at = date;
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
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        map.put("id", id);

        if (action != null) map.put("action", action);
        if (created_at != null) map.put("created_at", created_at);
        if (type != null) map.put("type", type);
        if (reference_id != null) map.put("reference_id", reference_id);

        if (content != null) {
            String jsonContent = null;
            try {
                jsonContent = new ObjectMapper().writeValueAsString(content);

                // Convert jsonContent to JSONB using
                SqlParameterValue jsonbParam = new SqlParameterValue(
                        Types.OTHER, // Tipe SQL: OTHER untuk JSONB
                        jsonContent
                );

                map.put("content", jsonbParam);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return map;
    }
}
