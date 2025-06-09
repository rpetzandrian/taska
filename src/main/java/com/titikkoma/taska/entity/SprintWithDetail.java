package com.titikkoma.taska.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Sprint;

import java.util.List;

public class SprintWithDetail extends Sprint {
    private String creatorName;
    private List<Log> logs;

    public SprintWithDetail(Sprint sprint, String creatorName, List<Log> logs) {
        super(
                sprint.getId(),
                sprint.getName(),
                sprint.getDescription(),
                sprint.getStart_date(),
                sprint.getEnd_date(),
                sprint.getStatus(),
                sprint.getOrganization_code(),
                sprint.getCreated_by()
        );
        this.creatorName = creatorName;
        this.logs = logs;
    }

    @JsonProperty("creator_name")
    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
