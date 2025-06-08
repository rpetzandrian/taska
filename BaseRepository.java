package com.titikkoma.taska.base;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

public class BaseRepository<T extends BaseEntity<ID>, ID> {
    protected final NamedParameterJdbcTemplate namedJdbcTemplate;
    protected final JdbcTemplate jdbcTemplate;
    protected final RowMapper<T> rowMapper;
    protected final String tableName;
    protected final String idColumnName;

    protected BaseRepository(JdbcTemplate jdbcTemplate,
                             String tableName,
                             String idColumnName,
                             RowMapper<T> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.tableName = tableName;
        this.idColumnName = idColumnName;
        this.rowMapper = rowMapper;
    }

    @Transactional(readOnly = true)
    public List<T> findAll(Map<String, Object> conditions) {
        String sql = String.format("SELECT * FROM %s",  tableName);
        String whereClause = conditions.keySet().stream()
                .map(o -> String.format("%s = :%s", o, o))
                .collect(Collectors.joining(" AND "));

        if (!whereClause.isEmpty()) {
            sql += " WHERE " + whereClause;
        }

        System.out.println(sql);

        try {
            return namedJdbcTemplate.query(sql, conditions, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public Optional<T> findById(ID id) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", tableName, idColumnName);
        try {
            T entity = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Transactional(readOnly = true)
    public Optional<T> findOne(Map<String, Object> conditions) {
        String whereClause = conditions.keySet().stream()
                .map(o -> String.format("%s = :%s", o, o))
                .collect(Collectors.joining(" AND "));

        String sql = String.format("SELECT * FROM %s WHERE %s LIMIT 1", tableName, whereClause);
        try {
            T entity = namedJdbcTemplate.queryForObject(sql, conditions, rowMapper);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public T findOneOrFail(Map<String, Object> conditions) {
        Optional<T> entity = findOne(conditions);
        System.out.println(entity);
        if (entity.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("%s_not_found", tableName));
        }

        return entity.orElse(null);
    }

    @Transactional
    public T create(T entity) {
        Map<String, Object> insertValues = entity.toInsertMap();
        List<String> columns = new ArrayList<>(insertValues.keySet());

        String insertColumns = String.join(", ", columns);
        String valuesPlaceholder = columns.stream()
                .map(col -> ":" + col)
                .collect(Collectors.joining(", "));

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)",
                tableName, insertColumns, valuesPlaceholder);

        SqlParameterSource params = new MapSqlParameterSource(insertValues);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        return entity;
    }

    @Transactional
    public int update(Map<String, Object> conditions, Map<String, Object> updates) {
        String setClause = updates.keySet().stream()
                .map(key -> String.format("%s = :%s", key, key))
                .collect(Collectors.joining(", "));

        String whereClause = buildWhereClause(conditions);

        String sql = String.format("UPDATE %s SET %s WHERE %s",
                tableName, setClause, whereClause);

        System.out.println(sql);

        Map<String, Object> params = new HashMap<>();
        params.putAll(conditions);
        params.putAll(updates);

        return namedJdbcTemplate.update(sql, params);
    }

    @Transactional
    public int delete(Map<String, Object> conditions) {
        String whereClause = buildWhereClause(conditions);
        String sql = String.format("DELETE FROM %s WHERE %s", tableName, whereClause);
        return namedJdbcTemplate.update(sql, conditions);
    }

    @Transactional
    public int update(Map<String, Object> updates, ID id) {
    Map<String, Object> conditions = new HashMap<>();
    conditions.put(idColumnName, id);
    return update(conditions, updates);
    }

    @Transactional
    public int deleteById(ID id) {
    Map<String, Object> conditions = new HashMap<>();
    conditions.put(idColumnName, id);
    return delete(conditions);
    }

//    @Transactional
//    public T upsert(Map<String, Object> searchConditions, T newEntity) {
//        return findOne(searchConditions)
//                .map(existing -> {
//                    update(searchConditions, newEntity.toUpdateMap());
//                    return existing;
//                })
//                .orElseGet(() -> create(newEntity));
//    }


    private String buildWhereClause(Map<String, Object> conditions) {
        return conditions.keySet().stream()
                .map(o -> String.format("%s = :%s", o, o))
                .collect(Collectors.joining(" AND "));
    }

    private String camelToSnakeCase(String str) {
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}



