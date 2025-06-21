package com.titikkoma.taska.service;

import com.titikkoma.taska.dto.CreateTaskRequestBody;
import com.titikkoma.taska.dto.TaskRequestFilterParams;
import com.titikkoma.taska.dto.UpdateTaskRequestBody;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Organization;
import com.titikkoma.taska.model.Task;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.LogRepository;
import com.titikkoma.taska.repository.OrganizationRepository;
import com.titikkoma.taska.repository.TaskRepository;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    private final OrganizationRepository organizationRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, LogRepository logRepository, OrganizationRepository organizationRepository) {
        this.taskRepository = taskRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
    }

    public List<TaskWithDetail> findAllTaskBySprintId(String sprintId, TaskRequestFilterParams query) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("sprint_id", sprintId);

        if (query.getAssignee_id() != null) conditions.put("assignee_id", query.getAssignee_id());
        if (query.getType() != null) conditions.put("type", query.getType());
        if (query.getStatus() != null) conditions.put("status", query.getStatus());

        return this.taskRepository.findAllWithDetails(conditions);
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

        Map<String, Object> orgCond = new HashMap<>();
        orgCond.put("code", principal.getOrganizationCode());
        Organization org = organizationRepository.findOneOrFail(orgCond);

        Integer counter = org.getCounter() + 1;
        Task payload = new Task(
                UUID.randomUUID().toString(),
                data.getName(),
                data.getSprint_id(),
                data.getDescription(),
                "todo",
                data.getPriority(),
                data.getStory_point(),
                principal.getId(),
                data.getAssignee_id(),
                data.getType(),
                org.getCode() + "-" + counter
        );

        this.taskRepository.create(payload);
        this.organizationRepository.updateCounter(org.getId(), counter);

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

        this.logRepository.create(logPayload);

        return payload;
    }

    public int updateTask(String id, UpdateTaskRequestBody data) {
        Map<String, Object> cond = new HashMap<>();
        cond.put("id", id);
        Task task = this.taskRepository.findOneOrFail(cond);

        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> updatePayload = new HashMap<>();
        if (data.getName() != null) { updatePayload.put("name", data.getName()); }
        if (data.getSprint_id() != null) { updatePayload.put("sprint_id", data.getSprint_id()); }
        if (data.getDescription() != null) { updatePayload.put("description", data.getDescription()); }
        if (data.getPriority() != null) { updatePayload.put("priority", data.getPriority()); }
        if (data.getStory_point() != null) { updatePayload.put("story_point", data.getStory_point()); }
        if (data.getAssignee_id() != null) { updatePayload.put("assignee_id", data.getAssignee_id()); }
        if (data.getType() != null) { updatePayload.put("type", data.getType()); }
        if (data.getStatus() != null) { updatePayload.put("status", data.getStatus()); }

        updatePayload.replaceAll((key, value) -> "".equals(value) ? null : value);

        Integer rows = this.taskRepository.update(cond, updatePayload);

        Map<String, Object> content = new HashMap<>();
        content.put("creator_id", principal.getId());
        content.put("creator_name", principal.getName());
        content.put("payload", updatePayload);

        if (!data.getStatus().equals(task.getStatus())) {
            content.put("old_status", task.getStatus());
            content.put("new_status", task.getStatus());
        }

        Log logPayload = new Log(
                UUID.randomUUID().toString(),
                "update",
                new Timestamp(Instant.now().toEpochMilli()),
                "task",
                task.getId(),
                content
        );

        this.logRepository.create(logPayload);

        return rows;
    }
}
