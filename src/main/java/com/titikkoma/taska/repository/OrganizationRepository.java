package com.titikkoma.taska.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.model.Organization;

@Repository
public class OrganizationRepository extends BaseRepository<Organization, String> {

    public OrganizationRepository(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "organizations", "id", new OrganizationRowMapper());
    }

    private static class OrganizationRowMapper implements RowMapper<Organization> {
        @Override
        public Organization mapRow(ResultSet rs, int rowNum) throws SQLException {
            Organization org = new Organization();
            org.setId(rs.getString("id"));
            org.setCode(rs.getString("code"));
            org.setName(rs.getString("name"));
            org.setDescription(rs.getString("description"));
            org.setCounter(rs.getInt("counter"));
            return org;
        }
    }

    public void updateCounter(String code, Integer counter) {
        Map<String, Object> cond = new HashMap<>();
        cond.put("code", code);

        Map<String, Object> updates = new HashMap<>();
        updates.put("counter", counter);

        String whereClause = this.buildWhereClause(cond);
        String sql = String.format("UPDATE %s SET counter = :counter WHERE %s",
                tableName, whereClause);

        Map<String, Object> params = new HashMap<>();
        params.putAll(cond);
        params.putAll(updates);

        this.namedJdbcTemplate.update(sql, params);
    }
}
