package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.CreateTaskRequestBody;
import com.titikkoma.taska.dto.TaskRequestFilterParams;
import com.titikkoma.taska.dto.UpdateTaskRequestBody;
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
    public WebResponse<List<TaskWithDetail>> findAllTaskBySprintId(
            @PathVariable String sprintId,
            TaskRequestFilterParams query
    ) {
        List<TaskWithDetail> tasks = this.taskService.findAllTaskBySprintId(sprintId, query);
        return WebResponse.<List<TaskWithDetail>>builder().data(tasks).build();
    }

    @GetMapping("/v1/task/{id}")
    public WebResponse<TaskWithDetail> findTaskDetailById(@PathVariable String id) {
        TaskWithDetail task = this.taskService.findTaskWithDetailById(id);
        return WebResponse.<TaskWithDetail>builder().data(task).build();
    }

    @PutMapping("/v1/task/{id}")
    public WebResponse<Integer> updateTask(@PathVariable String id, @RequestBody UpdateTaskRequestBody data) {
        int res = this.taskService.updateTask(id, data);
        return WebResponse.<Integer>builder().data(res).build();
    }
    @DeleteMapping("/v1/task/delete/{id}")
    public WebResponse<String> deleteSprint(@PathVariable String id) {
        this.taskService.deleteTask(id);
        return WebResponse.<String>builder()
                .data("Task has been successfully deleted.")
                .build();
    }
}
