package ninja.cero.data.sql.repository.support;

import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public class SqlRepositoryFactory extends RepositoryFactorySupport {
    private final RelationalMappingContext mappingContext;

    private final JdbcTemplate jdbcTemplate;

    public SqlRepositoryFactory(RelationalMappingContext mappingContext, JdbcTemplate jdbcTemplate) {
        this.mappingContext = mappingContext;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return null;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation metadata) {
        RelationalPersistentEntity<?> persistentEntity = mappingContext
                .getRequiredPersistentEntity(metadata.getDomainType());
        return getTargetRepositoryViaReflection(metadata, persistentEntity, mappingContext, jdbcTemplate);
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        return Optional.of(new SqlQueryLookupStrategy(jdbcTemplate));
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return DefaultRepository.class;
    }
}
