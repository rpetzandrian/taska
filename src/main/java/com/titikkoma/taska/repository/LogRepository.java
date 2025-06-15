package com.titikkoma.taska.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.model.Log;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class LogRepository extends BaseRepository<Log, String> {
    public LogRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "logs", "id", new LogRowMapper());
    }

    private static class LogRowMapper implements RowMapper<Log> {
        @Override
        public Log mapRow(ResultSet rs, int rowNum) throws SQLException {

            String jsonContent = rs.getString("content"); // Get raw JSON string
            ObjectMapper objectMapper = new ObjectMapper();

            Object content;
            try {
                content = objectMapper.readValue(jsonContent, Object.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to parse JSON", e);
            }

            Log log = new Log(
                    rs.getString("id"),
                    rs.getString("action"),
                    rs.getTimestamp("created_at"),
                    rs.getString("type"),
                    rs.getString("reference_id"),
                    content
            );

            return log;
        }
    }
}
