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

    private static class TaskRowMapper implements RowMapper<Task> {
        @Override
        public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
            Task task = new Task();

            task.setId(rs.getString("id"));
            task.setName(rs.getString("name"));
            task.setDescription(rs.getString("description"));
            task.setPriority(rs.getInt("priority"));
            task.setReporter_id(rs.getString("reporter_id"));
            task.setAssignee_id(rs.getString("assignee_id"));
            task.setStatus(rs.getString("status"));
            task.setStory_point(rs.getInt("story_point"));
            task.setType(rs.getString("type"));

            return task;
        }
    }
}
