package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.CreateTaskRequestBody;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Task;
import com.titikkoma.taska.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/v1/task")
    public WebResponse<Task> createTask(@Valid @RequestBody CreateTaskRequestBody data) {
        Task task = this.taskService.createNewTask(data);
        return WebResponse.<Task>builder().data(task).build();
    }

    @GetMapping("/v1/task/list/{sprintId}")
    public WebResponse<List<Task>> findAllTaskBySprintId(@PathVariable String sprintId) {
        List<Task> tasks = this.taskService.findAllTaskBySprintId(sprintId);
        return WebResponse.<List<Task>>builder().data(tasks).build();
    }

    @GetMapping("/v1/task/{id}")
    public WebResponse<TaskWithDetail> findTaskDetailById(@PathVariable String id) {
        TaskWithDetail task = this.taskService.findTaskWithDetailById(id);
        return WebResponse.<TaskWithDetail>builder().data(task).build();
    }
}
