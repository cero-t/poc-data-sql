package ninja.cero.data.jdbc_ext.repository.support;

import ninja.cero.data.jdbc_ext.repository.query.SqlFile;
import ninja.cero.data.jdbc_ext.repository.query.SqlFileQueryMethod;
import ninja.cero.data.jdbc_ext.spring_data_jdbc.repository.query.StringBasedJdbcQuery;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.lang.reflect.Method;

public class JdbcExtQueryLookupStrategy implements QueryLookupStrategy {
    NamedParameterJdbcOperations operations;
    RelationalMappingContext context;
    JdbcConverter converter;
    QueryLookupStrategy originalQueryLookupStrategy;
    QueryMethodEvaluationContextProvider evaluationContextProvider;

    public JdbcExtQueryLookupStrategy(NamedParameterJdbcOperations operations, RelationalMappingContext context,
                                      JdbcConverter converter, QueryMethodEvaluationContextProvider evaluationContextProvider,
                                      QueryLookupStrategy originalQueryLookupStrategy) {
        this.operations = operations;
        this.context = context;
        this.converter = converter;
        this.originalQueryLookupStrategy = originalQueryLookupStrategy;
        this.evaluationContextProvider = evaluationContextProvider;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        SqlFileQueryMethod queryMethod = new SqlFileQueryMethod(method, metadata, factory, namedQueries, this.context);
        EntityRowMapper<?> rowMapper = new EntityRowMapper<>(context.getRequiredPersistentEntity(metadata.getDomainType()), converter);

        SqlFile annotation = method.getAnnotation(SqlFile.class);
        if (annotation != null) {
            return new StringBasedJdbcQuery(queryMethod, operations, rowMapper, converter, evaluationContextProvider);
        }

        return originalQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
    }
}
