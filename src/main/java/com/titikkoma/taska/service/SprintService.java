package com.titikkoma.taska.service;

import com.titikkoma.taska.base.error.BadRequestError;
import com.titikkoma.taska.base.helpers.DateFormatter;
import com.titikkoma.taska.dto.CreateSprintRequestBody;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.LogRepository;
import com.titikkoma.taska.repository.SprintRepository;
import com.titikkoma.taska.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SprintService {

    private final SprintRepository sprintRepository;
    private final LogRepository logRepository;
    private final UserRepository userRepository;

    public SprintService(SprintRepository sprintRepository, LogRepository logRepository, UserRepository userRepository) {
        this.sprintRepository = sprintRepository;
        this.logRepository = logRepository;
        this.userRepository = userRepository;
    }

    public List<Sprint> findAllSprints() {
        CustomAuthPrincipal customAuthPrincipal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> params = new HashMap<>();
        params.put("organization_code", customAuthPrincipal.getOrganizationCode());

        return this.sprintRepository.findAll(params);
    }

    public SprintWithDetail findSprintWithDetailById(String id) {
        CustomAuthPrincipal customAuthPrincipal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> params = new HashMap<>();
        params.put("organization_code", customAuthPrincipal.getOrganizationCode());
        params.put("id", id);
        Sprint sprint = this.sprintRepository.findOneOrFail(params);

        Map<String, Object> userParams = new HashMap<>();
        userParams.put("id", sprint.getCreated_by());

        User user = this.userRepository.findOneOrFail(userParams);

        Map<String, Object> logParams = new HashMap<>();
        logParams.put("reference_id", id);
        logParams.put("type", "sprint");
        List<Log> logs = this.logRepository.findAll(logParams);

        return new SprintWithDetail(sprint, user.getName(), logs);
    }

    public Sprint createNewSprint(CreateSprintRequestBody data) {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && !principal.getRole().equals("admin")) {
            throw new BadRequestError("Only admins can create sprints");
        }

        Timestamp startDate = DateFormatter.formatDateToTimestamp(data.getStart_date(), "dd-MM-yyyy");
        Timestamp endDate = DateFormatter.formatDateToTimestamp(data.getEnd_date(), "dd-MM-yyyy");
        assert principal != null;
        Sprint sprint = new Sprint(
                UUID.randomUUID().toString(),
                data.getName(),
                data.getDescription(),
                startDate,
                endDate,
                "new",
                principal.getOrganizationCode(),
                principal.getId()
        );

        Sprint created = this.sprintRepository.create(sprint);

        Map<String, Object> content = new HashMap<>();
        content.put("creator_id", principal.getId());
        content.put("creator_name", principal.getName());
        Log logPayload = new Log(
                UUID.randomUUID().toString(),
                "create",
                new Timestamp(Instant.now().toEpochMilli()),
                "sprint",
                sprint.getId(),
                content
        );

        this.logRepository.create(logPayload);

        return created;
    }

    public SprintWithDetail findCurrentSprint() {
        CustomAuthPrincipal customAuthPrincipal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> params = new HashMap<>();
        params.put("organization_code", customAuthPrincipal.getOrganizationCode());
        params.put("status", "active");
        Sprint sprint = this.sprintRepository.findOneOrFail(params);

        System.out.println("sprint: " + sprint.getCreated_by());

        Map<String, Object> userParams = new HashMap<>();
        userParams.put("id", sprint.getCreated_by());

        User user = this.userRepository.findOneOrFail(userParams);

        Map<String, Object> logParams = new HashMap<>();
        logParams.put("reference_id", sprint.getId());
        logParams.put("type", "sprint");
        List<Log> logs = this.logRepository.findAll(logParams);

        return new SprintWithDetail(sprint, user.getName(), logs);
    }
}
