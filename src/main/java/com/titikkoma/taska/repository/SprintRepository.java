package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.entity.SprintWithDetail;
import com.titikkoma.taska.entity.TaskWithDetail;
import com.titikkoma.taska.model.Log;
import com.titikkoma.taska.model.Sprint;
import com.titikkoma.taska.model.Task;
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
public class SprintRepository extends BaseRepository<Sprint, String> {

    public SprintRepository(org.springframework.jdbc.core.JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "sprints", "id", new SprintRowMapper());
    }

    private static class SprintRowMapper implements RowMapper<Sprint> {
        @Override
        public Sprint mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Sprint(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getTimestamp("start_date"),
                    rs.getTimestamp("end_date"),
                    rs.getString("status"),
                    rs.getString("organization_code"),
                    rs.getString("created_by")
            );
        }
    }

    private static class SprintWithDetailMapper implements RowMapper<SprintWithDetail> {
        @Override
        public SprintWithDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
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

            return new SprintWithDetail(
                    sprint,
                    rs.getString("creator_name"),
                    new ArrayList<Log>()
            );
        }
    }

    @Transactional(readOnly = true)
    public List<SprintWithDetail> findAllWithDetails(Map<String, Object> conditions) {
        String sql = String.format("""
            SELECT
                t.*,
                creator.name AS creator_name
            FROM
                %1$s t
            LEFT JOIN
                users creator ON t.created_by = creator.id
            """, tableName);

        String whereClause = conditions.keySet().stream()
                .map(key -> String.format("t.%s = :%s", key, key))
                .collect(Collectors.joining(" AND "));

        if (!whereClause.isEmpty()) {
            sql += " WHERE " + whereClause;
        }

        sql += """
         ORDER BY
            CASE
                WHEN t.status = 'active' THEN 1
                WHEN t.status = 'new' THEN 2
                WHEN t.status = 'completed' THEN 3
                ELSE 4
            END,
            t.name ASC
        """;

        SprintWithDetailMapper mapper = new SprintWithDetailMapper();

        try {
            return namedJdbcTemplate.query(sql, conditions, mapper);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
