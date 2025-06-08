package com.titikkoma.taska.base;

import java.util.List;
import java.util.Map;

// Base Entity Interface
public interface BaseEntity<ID> {
    ID getId();
    void setId(ID id);

    Map<String, Object> toInsertMap();
}