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
            Sprint sprint = new Sprint();
            sprint.setId(rs.getString("id"));
            sprint.setName(rs.getString("name"));
            sprint.setDescription(rs.getString("description"));
            sprint.setStatus(rs.getString("status"));
            sprint.setStart_date(rs.getTimestamp("start_date"));
            sprint.setEnd_date(rs.getTimestamp("end_date"));
            sprint.setCreated_by(rs.getString("created_by"));
            sprint.setOrganization_code(rs.getString("organization_code"));

            return sprint;
        }
    }

}
