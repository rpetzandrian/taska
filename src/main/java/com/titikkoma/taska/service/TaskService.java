package com.titikkoma.taska.service;

import com.titikkoma.taska.dto.CreateTaskRequestBody;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Task;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.LogRepository;
import com.titikkoma.taska.repository.TaskRepository;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
        Optional<Task> task = this.taskRepository.findById(id);
        if (task.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task with id " + id + " not found");
        }

        String reporterId = task.get().getReporter_id();
        String assigneeId = task.get().getAssignee_id();
        Optional<User> reporter = this.userRepository.findById(reporterId);
        Optional<User> assignee = this.userRepository.findById(assigneeId);

        String assigneeName = "";
        if (assignee.isPresent()) {
            assigneeName = assignee.get().getName();
        }

        Map<String, Object> logCond = new HashMap<>();
        logCond.put("reference_id", id);
        logCond.put("type", "task");

        List<Log> logs = this.logRepository.findAll(logCond);

        return new TaskWithDetail(
                task.get(),
                reporter.get().getName(),
                assigneeName,
                logs
        );
    }

    public Task createNewTask(CreateTaskRequestBody data) {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Task payload = new Task(
                UUID.randomUUID().toString(),
                data.getName(),
                data.getSprint_id(),
                data.getDescription(),
                "new",
                data.getPriority(),
                data.getStory_point(),
                principal.getId(),
                data.getAssignee_id(),
                data.getType()
        );

        System.out.println(payload);

        this.taskRepository.create(payload);

        Map<String, Object> content = new HashMap<>();
        content.put("creator_id", principal.getId());
        content.put("creator_name", principal.getName());
        Log logPayload = new Log(
                UUID.randomUUID().toString(),
                "create",
                new Timestamp(Instant.now().toEpochMilli()),
                "task",
                payload.getId(),
                content
        );

        System.out.println(logPayload);

        this.logRepository.create(logPayload);

        return payload;
    }
}
