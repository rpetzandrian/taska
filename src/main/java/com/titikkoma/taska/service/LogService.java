package com.titikkoma.taska.service;

import com.titikkoma.taska.entity.Log;
import com.titikkoma.taska.repository.LogRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LogService {
    private LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public List<Log> findAllLogs() {
        Map<String, Object> logCond = new HashMap<>();
        return logRepository.findAll(logCond);
    }
}
