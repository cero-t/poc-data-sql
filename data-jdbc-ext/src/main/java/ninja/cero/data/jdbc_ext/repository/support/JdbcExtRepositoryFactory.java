package ninja.cero.data.jdbc_ext.repository.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class JdbcExtRepositoryFactory extends JdbcRepositoryFactory {
    private final JdbcTemplate jdbcTemplate;
    private final RelationalMappingContext context;
    private final JdbcConverter converter;
    private final ApplicationEventPublisher publisher;
    private final DataAccessStrategy accessStrategy;

    private EntityCallbacks entityCallbacks;

    public JdbcExtRepositoryFactory(JdbcTemplate jdbcTemplate, DataAccessStrategy dataAccessStrategy, RelationalMappingContext context, JdbcConverter converter, Dialect dialect, ApplicationEventPublisher publisher, NamedParameterJdbcOperations operations) {
        super(dataAccessStrategy, context, converter, dialect, publisher, operations);

        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
        this.context = context;
        this.converter = converter;
        this.accessStrategy = dataAccessStrategy;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        JdbcAggregateTemplate template = new JdbcAggregateTemplate(publisher, context, converter, accessStrategy);

        if (entityCallbacks != null) {
            template.setEntityCallbacks(entityCallbacks);
        }

        RelationalPersistentEntity<?> persistentEntity = context
                .getRequiredPersistentEntity(repositoryInformation.getDomainType());

        return getTargetRepositoryViaReflection(repositoryInformation, jdbcTemplate, template, persistentEntity,
                converter);
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key,
                                                                   QueryMethodEvaluationContextProvider evaluationContextProvider) {
        Optional<QueryLookupStrategy> original = super.getQueryLookupStrategy(key, evaluationContextProvider);
        return Optional.of(new JdbcExtQueryLookupStrategy(jdbcTemplate, context, converter, original.orElseThrow()));
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata) {
        return JdbcExtDefaultRepository.class;
    }

    @Override
    public void setBeanFactory(@Nullable BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
    }

    @Override
    public void setQueryMappingConfiguration(QueryMappingConfiguration queryMappingConfiguration) {
        super.setQueryMappingConfiguration(queryMappingConfiguration);
    }

    @Override
    public void setEntityCallbacks(EntityCallbacks entityCallbacks) {
        this.entityCallbacks = entityCallbacks;
        super.setEntityCallbacks(entityCallbacks);
    }
}
