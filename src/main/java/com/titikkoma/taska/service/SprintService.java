package com.titikkoma.taska.service;

import com.titikkoma.taska.base.error.BadRequestError;
import com.titikkoma.taska.base.helpers.DateFormatter;
import com.titikkoma.taska.dto.CreateSprintRequestBody;
import com.titikkoma.taska.dto.UpdateSprintRequestBody;
import com.titikkoma.taska.entity.CustomAuthPrincipal;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.model.User;
import com.titikkoma.taska.repository.LogRepository;
import com.titikkoma.taska.repository.SprintRepository;
import com.titikkoma.taska.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.Instant;
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

    public List<SprintWithDetail> findAllSprints() {
        CustomAuthPrincipal customAuthPrincipal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> params = new HashMap<>();
        params.put("organization_code", customAuthPrincipal.getOrganizationCode());

        return this.sprintRepository.findAllWithDetails(params);
    }

    public List<Sprint> findAllSprintConfigs() {
        CustomAuthPrincipal customAuthPrincipal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Map<String, Object> params = new HashMap<>();
        List<String> status = new ArrayList<>();
        status.add("active");
        status.add("new");

        params.put("organization_code", customAuthPrincipal.getOrganizationCode());
        params.put("status", status);

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Only admins can create sprints");
        }

        Timestamp startDate = DateFormatter.formatDateToTimestamp(data.getStart_date(), "dd-MM-yyyy");
        Timestamp endDate = DateFormatter.formatDateToTimestamp(data.getEnd_date(), "dd-MM-yyyy");

        if (startDate.after(endDate)) {
            throw new BadRequestError("Start date and end date must be before end date");
        }

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

    public Sprint updateSprint(String id, UpdateSprintRequestBody data) {
        // 1. Dapatkan informasi user yang sedang login dan periksa rolenya
        CustomAuthPrincipal principal = (CustomAuthPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || !principal.getRole().equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only admins can update sprints");
        }

        // 2. Buat parameter untuk mencari sprint
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("id", id);
        searchParams.put("organization_code", principal.getOrganizationCode());
        
        // 3. Cari sprint yang ada untuk perbandingan dan validasi
        Sprint existingSprint = this.sprintRepository.findOneOrFail(searchParams);

        // 4. Siapkan Map untuk menampung field yang akan di-update (untuk klausa SET)
        //    dan Map untuk mencatat perubahan (untuk log)
        Map<String, Object> updates = new HashMap<>();
        Map<String, Object> changesForLog = new HashMap<>();

        // 5. Bandingkan dan tambahkan perubahan ke Map 'updates' dan 'changesForLog'
        if (data.getName() != null && !data.getName().equals(existingSprint.getName())) {
            updates.put("name", data.getName());
            changesForLog.put("name", Map.of("old", existingSprint.getName(), "new", data.getName()));
        }
        if (data.getDescription() != null && !data.getDescription().equals(existingSprint.getDescription())) {
            updates.put("description", data.getDescription());
            changesForLog.put("description", Map.of("old", existingSprint.getDescription(), "new", data.getDescription()));
        }

        Timestamp newStartDate = null;
        if (data.getStart_date() != null) {
            newStartDate = DateFormatter.formatDateToTimestamp(data.getStart_date(), "dd-MM-yyyy");
            if (!newStartDate.equals(existingSprint.getStart_date())) {
                updates.put("start_date", newStartDate);
                changesForLog.put("start_date", Map.of("old", existingSprint.getStart_date(), "new", newStartDate));
            }
        }

        Timestamp newEndDate = null;
        if (data.getEnd_date() != null) {
            newEndDate = DateFormatter.formatDateToTimestamp(data.getEnd_date(), "dd-MM-yyyy");
            if (!newEndDate.equals(existingSprint.getEnd_date())) {
                updates.put("end_date", newEndDate);
                changesForLog.put("end_date", Map.of("old", existingSprint.getEnd_date(), "new", newEndDate));
            }
        }
        
        // 6. Validasi tanggal akhir berdasarkan nilai final (baik dari data lama maupun baru)
        Timestamp finalStartDate = (Timestamp) updates.getOrDefault("start_date", existingSprint.getStart_date());
        Timestamp finalEndDate = (Timestamp) updates.getOrDefault("end_date", existingSprint.getEnd_date());
        if (finalStartDate.after(finalEndDate)) {
            throw new BadRequestError("Start date cannot be after end date");
        }

        // 7. Jika tidak ada yang di-update, langsung kembalikan data yang ada
        if (updates.isEmpty()) {
            return existingSprint;
        }

        // 8. Panggil repository.update dengan format yang benar
        //    'searchParams' menjadi 'conditions' (klausa WHERE)
        //    'updates' menjadi 'updates' (klausa SET)
        int affectedRows = this.sprintRepository.update(searchParams, updates);

        if (affectedRows > 0) {
            // 9. Buat log jika update berhasil
            Map<String, Object> logContent = new HashMap<>();
            logContent.put("updater_id", principal.getId());
            logContent.put("updater_name", principal.getName());
            logContent.put("changes", changesForLog);

            Log logPayload = new Log(
                    UUID.randomUUID().toString(),
                    "update",
                    new Timestamp(Instant.now().toEpochMilli()),
                    "sprint",
                    id, // reference_id adalah id sprint
                    logContent
            );
            this.logRepository.create(logPayload);

            // 10. Karena 'update' mengembalikan int, kita perlu mengambil ulang data
            //     untuk mendapatkan objek Sprint yang sudah ter-update sepenuhnya.
            return this.sprintRepository.findOneOrFail(searchParams);
        }

        // Jika tidak ada baris yang terpengaruh, kembalikan data lama
        return existingSprint;
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
