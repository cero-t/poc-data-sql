package ninja.cero.data.sql.repository.support;

import ninja.cero.data.sql.core.convert.RecordMapper;
import ninja.cero.data.sql.repository.SqlRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PersistentPropertyPath;
import org.springframework.data.relational.core.dialect.H2Dialect;
import org.springframework.data.relational.core.dialect.RenderContextFactory;
import org.springframework.data.relational.core.mapping.PersistentPropertyPathExtension;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.relational.core.sql.*;
import org.springframework.data.relational.core.sql.render.SqlRenderer;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DefaultRepository<T, ID> implements SqlRepository<T, ID> {
    private final RelationalPersistentEntity<T> entity;

    private final RelationalMappingContext mappingContext;

    private final SqlRenderer sqlRenderer;

    private final JdbcTemplate jdbcTemplate;

    private final RecordMapper<T> recordMapper;

    public DefaultRepository(RelationalPersistentEntity<T> entity, RelationalMappingContext mappingContext, JdbcTemplate jdbcTemplate) {
        this.entity = entity;
        this.mappingContext = mappingContext;
        this.sqlRenderer = SqlRenderer.create(new RenderContextFactory(H2Dialect.INSTANCE).createRenderContext());
        this.jdbcTemplate = jdbcTemplate;
        recordMapper = new RecordMapper<>(entity.getType());
    }

    @Override
    public <S extends T> S save(S entity) {
        return null;
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public List<T> findAll() {
        Table table = Table.create(entity.getQualifiedTableName());

        List<Expression> columnExpressions = new ArrayList<>();
        for (PersistentPropertyPath<RelationalPersistentProperty> path : mappingContext
                .findPersistentPropertyPaths(entity.getType(), p -> true)) {
            PersistentPropertyPathExtension extPath = new PersistentPropertyPathExtension(mappingContext, path);
            Column column = table.column(extPath.getColumnName()).as(extPath.getColumnAlias());
            columnExpressions.add(column);
        }

        Select select = StatementBuilder.select(columnExpressions).from(table).build();
        String sql = sqlRenderer.render(select);

        return jdbcTemplate.query(sql, recordMapper);
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends ID> ids) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<T> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends T, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<T> query(String query, Object... args) {
        return jdbcTemplate.query(query, recordMapper, args);
    }
}
