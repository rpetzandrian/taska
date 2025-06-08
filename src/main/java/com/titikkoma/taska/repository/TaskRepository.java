package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class TaskRepository extends BaseRepository<Task, String> {
    public TaskRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "tasks", "id", new TaskRowMapper());
    }

    public int updateStatusById(String taskId, String newStatus) {
        String sql = "UPDATE tasks SET status = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newStatus, taskId);
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
                    rs.getString("type")
            );
        }
    }
}
