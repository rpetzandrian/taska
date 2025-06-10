package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.dto.CreateSprintRequestBody;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.service.SprintService;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
public class SprintController {
    SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @GetMapping("/v1/sprint/list")
    public WebResponse<List<SprintWithDetail>> findAllSprints() {
        List<SprintWithDetail> sprint = this.sprintService.findAllSprints();
        return WebResponse.<List<SprintWithDetail>>builder().data(sprint).build();
    }

    @GetMapping("/v1/sprint/config")
    public WebResponse<List<Sprint>> findAllSprintConfigs() {
        List<Sprint> sprint = this.sprintService.findAllSprintConfigs();
        return WebResponse.<List<Sprint>>builder().data(sprint).build();
    }

    @GetMapping("/v1/sprint/current")
    public WebResponse<SprintWithDetail> findCurrentSprint() {
        SprintWithDetail sprint = this.sprintService.findCurrentSprint();
        return WebResponse.<SprintWithDetail>builder().data(sprint).build();
    }

    @GetMapping("/v1/sprint/{id}")
    public WebResponse<SprintWithDetail> findSprintById(@PathVariable String id) {
        SprintWithDetail sprint = this.sprintService.findSprintWithDetailById(id);
        return WebResponse.<SprintWithDetail>builder().data(sprint).build();
    }

    @PostMapping("/v1/sprint")
    public WebResponse<Sprint> createSprint(@RequestBody CreateSprintRequestBody body) {
        Sprint sprint = this.sprintService.createNewSprint(body);
        return WebResponse.<Sprint>builder().data(sprint).build();
    }

}
