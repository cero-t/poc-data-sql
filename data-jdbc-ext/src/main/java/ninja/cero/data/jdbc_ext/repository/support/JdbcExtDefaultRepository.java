package ninja.cero.data.jdbc_ext.repository.support;

import ninja.cero.data.jdbc_ext.repository.JdbcExtRepository;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.List;

public class JdbcExtDefaultRepository<T, ID> extends ListSimpleJdbcRepository<T, ID> implements JdbcExtRepository<T, ID> {
    JdbcOperations jdbcOperations;
    JdbcAggregateOperations entityOperations;
    EntityRowMapper<T> entityRowMapper;

    public JdbcExtDefaultRepository(JdbcOperations jdbcOperations, JdbcAggregateOperations entityOperations, RelationalPersistentEntity<T> entity, JdbcConverter converter) {
        super(entityOperations, entity, converter);
        this.jdbcOperations = jdbcOperations;
        this.entityOperations = entityOperations;
        this.entityRowMapper = new EntityRowMapper<>(entity, converter);
    }

    @Override
    public List<T> query(String query, Object... args) {
        return jdbcOperations.query(query, entityRowMapper, args);
    }

    @Override
    public <S extends T> S insert(S entity) {
        return entityOperations.insert(entity);
    }
}
