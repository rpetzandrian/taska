package com.titikkoma.taska.model;

import com.titikkoma.taska.base.BaseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Organization implements BaseEntity<String> {
    private String id;
    private String code;
    private String name;
    private String description;
    private Integer counter;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
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

    public Integer getCounter() { return counter; }

    public void setCounter(Integer counter) { this.counter = counter; }

    @Override
    public Map<String, Object> toInsertMap() {
        Map<String, Object> map = new HashMap<>();
        if (id == null) {
            id = UUID.randomUUID().toString();
        }
        map.put("id", id);
        if (code != null) map.put("code", code);
        if (name != null) map.put("name", name);
        if (description != null) map.put("description", description);
        if (counter != null) map.put("counter", counter);
        return map;
    }
}
