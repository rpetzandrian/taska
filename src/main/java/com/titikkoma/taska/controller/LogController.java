package com.titikkoma.taska.controller;

import com.titikkoma.taska.base.WebResponse;
import com.titikkoma.taska.entity.Log;
import com.titikkoma.taska.service.LogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LogController {
    LogService logService;

    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/v1/logs/test")
    public WebResponse<List<Log>> findAllLogs() {
        List<Log> logs = logService.findAllLogs();
        return WebResponse.<List<Log>>builder().data(logs).build();
    }
}
