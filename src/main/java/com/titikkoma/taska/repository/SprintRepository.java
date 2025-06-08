package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.model.Sprint;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class SprintRepository extends BaseRepository<Sprint, String> {

    public SprintRepository(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "sprints", "id", new SprintRowMapper());
    }

    private static class SprintRowMapper implements RowMapper<Sprint> {
        @Override
        public Sprint mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sprint sprint = new Sprint(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getString("status"),
                    rs.getString("organization_code"),
                    rs.getString("created_by")
            );

            return sprint;
        }
    }

}
