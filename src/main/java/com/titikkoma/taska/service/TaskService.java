package com.titikkoma.taska.service;

import com.titikkoma.taska.base.error.BadRequestError;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Task;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.LogRepository;
import com.titikkoma.taska.repository.TaskRepository;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, LogRepository logRepository) {
        this.taskRepository = taskRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    public List<Task> findAllTaskBySprintId(String sprintId) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("sprint_id", sprintId);

        return this.taskRepository.findAll(conditions);
    }

    public TaskWithDetail findTaskWithDetailById(String id) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("id", id);

        Optional<Task> task = this.taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task with id " + id + " not found");
        }

        String reporterId = task.get().getReporter_id();
        String assigneeId = task.get().getAssignee_id();
        Optional<User> reporter = this.userRepository.findById(reporterId);
        Optional<User> assignee = this.userRepository.findById(assigneeId);

        Map<String, Object> logCond = new HashMap<>();
        logCond.put("reference_id", id);
        logCond.put("type", "task");

        List<Log> Logs = this.logRepository.findAll(logCond);

        return TaskWithDetail.buildTaskWithDetail(task.get(), Logs, reporter.get().getName(), assignee.get().getName());
    }
}
