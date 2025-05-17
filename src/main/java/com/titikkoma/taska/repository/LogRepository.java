package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.entity.Log;
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
            Log log = new Log();

            log.setId(rs.getString("id"));
            log.setAction(rs.getString("action"));
            log.setDate(rs.getString("date"));
            log.setBy(rs.getString("by"));
            log.setType(rs.getString("type"));
            return log;
        }
    }
}
