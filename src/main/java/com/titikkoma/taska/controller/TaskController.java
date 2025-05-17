package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Task;
import com.titikkoma.taska.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TaskController {
    TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/v1/tasks/list/{sprintId}")
    public WebResponse<List<Task>> findAllTaskBySprintId(@PathVariable String sprintId) {
        List<Task> tasks = this.taskService.findAllTaskBySprintId(sprintId);
        return WebResponse.<List<Task>>builder().data(tasks).build();
    }

    @GetMapping("/v1/tasks/{id}")
    public WebResponse<TaskWithDetail> findTaskDetailById(@PathVariable String id) {
        TaskWithDetail task = this.taskService.findTaskWithDetailById(id);
        return WebResponse.<TaskWithDetail>builder().data(task).build();
    }
}
