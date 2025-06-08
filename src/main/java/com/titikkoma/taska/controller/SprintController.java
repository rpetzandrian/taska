package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.service.SprintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SprintController {
    SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/v1/sprint/list")
    public WebResponse<List<Sprint>> findAllSprints() {
        List<Sprint> sprint = this.sprintService.findAllSprints();
        return WebResponse.<List<Sprint>>builder().data(sprint).build();
    }

    @GetMapping("/v1/sprint/{id}")
    public WebResponse<SprintWithDetail> findSprintById(@PathVariable String id) {
        SprintWithDetail sprint = this.sprintService.findSprintWithDetailById(id);
        return WebResponse.<SprintWithDetail>builder().data(sprint).build();
    }

     @DeleteMapping("/v1/sprint/{id}")
    public WebResponse<Void> deleteSprint(@PathVariable String id) {
        this.sprintService.deleteSprint(id);
        return WebResponse.<Void>builder().build();
    }

     @PutMapping("/v1/sprint/{id}")
    public WebResponse<Void> updateSprint(@PathVariable String id, @RequestBody Sprint sprint) {
        sprint.setId(id);
        this.sprintService.updateSprint(sprint);
        return WebResponse.<Void>builder().build();
    }

}
