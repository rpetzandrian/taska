package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TaskRepository extends BaseRepository<Task, String> {
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "tasks", "id", new TaskRowMapper());
    }

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Task(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("sprint_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("priority"),
                    rs.getInt("story_point"),
                    rs.getString("reporter_id"),
                    rs.getString("assignee_id"),
                    rs.getString("type"),
                    rs.getString("code")
            );
        }
    }

    private static class TaskWithDetailRowMapper implements RowMapper<TaskWithDetail> {
        @Override
        public TaskWithDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task =  new Task(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("sprint_id"),
                    rs.getString("description"),
                    rs.getString("status"),
                    rs.getInt("priority"),
                    rs.getInt("story_point"),
                    rs.getString("reporter_id"),
                    rs.getString("assignee_id"),
                    rs.getString("type"),
                    rs.getString("code")
            );

            return new TaskWithDetail(
                    task,
                    rs.getString("reporter_name"),
                    rs.getString("assignee_name"),
                    new ArrayList<>()
            );
        }
    }

    @Transactional(readOnly = true)
    public List<TaskWithDetail> findAllWithDetails(Map<String, Object> conditions) {
        String sql = String.format("""
            SELECT
                t.*,
                assignee.name AS assignee_name,
                reporter.name AS reporter_name
            FROM
                %1$s t
            LEFT JOIN
                users assignee ON t.assignee_id = assignee.id
            LEFT JOIN
                users reporter ON t.reporter_id = reporter.id
            """, tableName);

        String whereClause = conditions.keySet().stream()
                .map(key -> String.format("t.%s = :%s", key, key))
                .collect(Collectors.joining(" AND "));

        if (!whereClause.isEmpty()) {
            sql += " WHERE " + whereClause;
        }

        TaskWithDetailRowMapper taskWithAssigneeMapper = new TaskWithDetailRowMapper();

        try {
            return namedJdbcTemplate.query(sql, conditions, taskWithAssigneeMapper);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
