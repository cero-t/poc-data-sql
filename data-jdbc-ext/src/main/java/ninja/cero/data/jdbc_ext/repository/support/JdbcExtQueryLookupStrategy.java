package ninja.cero.data.jdbc_ext.repository.support;

import ninja.cero.data.jdbc_ext.repository.query.SqlFile;
import ninja.cero.data.jdbc_ext.repository.query.SqlFileQuery;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Method;

public class JdbcExtQueryLookupStrategy implements QueryLookupStrategy {
    JdbcTemplate jdbcTemplate;
    RelationalMappingContext context;
    JdbcConverter converter;
    QueryLookupStrategy originalQueryLookupStrategy;

    public JdbcExtQueryLookupStrategy(JdbcTemplate jdbcTemplate, RelationalMappingContext context, JdbcConverter converter, QueryLookupStrategy originalQueryLookupStrategy) {
        this.jdbcTemplate = jdbcTemplate;
        this.context = context;
        this.converter = converter;
        this.originalQueryLookupStrategy = originalQueryLookupStrategy;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        QueryMethod queryMethod = new QueryMethod(method, metadata, factory);
        EntityRowMapper<?> rowMapper = new EntityRowMapper<>(context.getRequiredPersistentEntity(metadata.getDomainType()), converter);

        SqlFile annotation = method.getAnnotation(SqlFile.class);
        if (annotation != null) {
            return new SqlFileQuery(annotation, jdbcTemplate, rowMapper, queryMethod);
        }

        return originalQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
    }

}
