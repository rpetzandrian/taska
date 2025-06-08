package com.titikkoma.taska.service;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Sprint createNewSprint(String name, Timestamp startDate, Timestamp endDate) {
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Sprint name cannot be null or empty");
        }
        if (startDate == null || endDate == null || !endDate.after(startDate)) {
            throw new IllegalArgumentException("Invalid start or end date");
        }

        Sprint sprint = new Sprint();
        sprint.setName(name);
        sprint.setStart_date(startDate);
        sprint.setEnd_date(endDate);
        sprint.setStatus("active");
        sprint.setOrganization_code(principal.getOrganizationCode());
        sprint.setCreated_by(principal.getId());

        this.sprintRepository.create(sprint);

        return sprint;
    }
}
