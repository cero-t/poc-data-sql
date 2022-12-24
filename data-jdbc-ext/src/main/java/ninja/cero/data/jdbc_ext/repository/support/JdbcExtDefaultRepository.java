package ninja.cero.data.jdbc_ext.repository.support;

import ninja.cero.data.jdbc_ext.repository.JdbcExtRepository;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcExtDefaultRepository<T, ID> extends SimpleJdbcRepository<T, ID> implements JdbcExtRepository<T, ID> {
    JdbcTemplate jdbcTemplate;

    RelationalPersistentEntity<T> entity;

    JdbcConverter jdbcConverter;

    public JdbcExtDefaultRepository(JdbcTemplate jdbcTemplate, JdbcAggregateOperations entityOperations, RelationalPersistentEntity<T> entity, JdbcConverter converter) {
        super(entityOperations, entity, converter);
        this.jdbcTemplate = jdbcTemplate;
        this.entity = entity;
        this.jdbcConverter = converter;
    }

    @Override
    public List<T> query(String query, Object... args) {
        return jdbcTemplate.query(query, getEntityRowMapper(), args);
    }

    private EntityRowMapper<T> getEntityRowMapper() {
        return new EntityRowMapper<>(entity, jdbcConverter);
    }
}
