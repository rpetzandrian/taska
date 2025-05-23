package com.titikkoma.taska.entity;

import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Sprint;

import java.util.List;

public class SprintWithDetail extends Sprint {
    private String creatorName;
    private List<Log> logs;

    public SprintWithDetail(Sprint sprint, String creatorName, List<Log> logs) {
        this.creatorName = creatorName;
        this.logs = logs;

        this.setId(sprint.getId());
        this.setName(sprint.getName());
        this.setDescription(sprint.getDescription());
        this.setOrganization_code(sprint.getOrganization_code());
        this.setStatus(sprint.getStatus());
        this.setStart_date(sprint.getStart_date());
        this.setEnd_date(sprint.getEnd_date());
        this.setCreated_by(sprint.getCreated_by());
    }

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
