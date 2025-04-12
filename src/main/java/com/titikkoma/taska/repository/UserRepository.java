package com.titikkoma.taska.repository;

import com.titikkoma.taska.base.BaseRepository;
import com.titikkoma.taska.entity.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository extends BaseRepository<User, String> {
    public UserRepository(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate, "users", "id", new UserRowMapper());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, "email");
        try {
            User entity = jdbcTemplate.queryForObject(sql, rowMapper, email);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}