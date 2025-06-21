package com.titikkoma.taska.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Task;

import java.util.List;

public class TaskWithDetail extends Task {
    private String reporterName;
    private String assigneeName;
    private List<Log> logs;

    public TaskWithDetail(
            Task task,
            String reporterName,
            String assigneeName,
            List<Log> logs
    ) {
        super(
                task.getId(),
                task.getName(),
                task.getSprint_id(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getStory_point(),
                task.getReporter_id(),
                task.getAssignee_id(),
                task.getType(),
                task.getCode()
        );
        this.reporterName = reporterName;
        this.assigneeName = assigneeName;
        this.logs = logs;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    @JsonProperty("reporter_name")
    public String getReporterName() {
        return reporterName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    @JsonProperty("assignee_name")
    public String getAssigneeName() {
        return assigneeName;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Log> getLogs() {
        return logs;
    }
}
