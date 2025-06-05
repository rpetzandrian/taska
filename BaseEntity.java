package com.titikkoma.taska.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Base Entity Interface
public interface BaseEntity<ID> {
    ID getId();
    void setId(ID id);

    Map<String, Object> toInsertMap();

    default Map<String, Object> toUpdateMap() {
        return new HashMap<>();
    }

    default Map<String, Object> toDeleteMap() {
        return new HashMap<>();
    }
}