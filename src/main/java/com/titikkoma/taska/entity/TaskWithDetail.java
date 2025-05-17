package com.titikkoma.taska.entity;

import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Task;

import java.util.List;

public class TaskWithDetail extends Task {
    private String reporterName;
    private String assigneeName;
    private List<Log> logs;

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public static TaskWithDetail buildTaskWithDetail(Task task, List<Log> logs, String reporterName, String assigneeName) {
        TaskWithDetail taskWithDetail = new TaskWithDetail();
        taskWithDetail.setId(task.getId());
        taskWithDetail.setName(task.getName());
        taskWithDetail.setDescription(task.getDescription());
        taskWithDetail.setStatus(task.getStatus());
        taskWithDetail.setStory_point(task.getStory_point());
        taskWithDetail.setType(task.getType());
        taskWithDetail.setPriority(task.getPriority());

        taskWithDetail.setReporterName(reporterName);
        taskWithDetail.setAssigneeName(assigneeName);
        taskWithDetail.setLogs(logs);

        return taskWithDetail;
    }
}
