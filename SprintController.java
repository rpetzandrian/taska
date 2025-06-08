package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.service.SprintService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SprintController {
    SprintService sprintService;

    public SprintController(SprintService sprintService) {
        this.sprintService = sprintService;
    }

    @PostMapping("/v1/sprint")
    public WebResponse<Sprint> createSprint(@RequestBody Map<String, Object> requestBody) {
        String name = (String) requestBody.get("name");
        Timestamp startDate = Timestamp.valueOf((String) requestBody.get("start_date"));
        Timestamp endDate = Timestamp.valueOf((String) requestBody.get("end_date"));

        Sprint sprint = this.sprintService.createNewSprint(name, startDate, endDate);
        return WebResponse.<Sprint>builder().data(sprint).build();
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

}
