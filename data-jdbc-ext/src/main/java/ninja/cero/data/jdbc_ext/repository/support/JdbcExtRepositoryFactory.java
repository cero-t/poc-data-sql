package ninja.cero.data.jdbc_ext.repository.support;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Optional;

public class JdbcExtRepositoryFactory extends JdbcRepositoryFactory {
    private final RelationalMappingContext context;
    private final JdbcConverter converter;
    private final ApplicationEventPublisher publisher;
    private final DataAccessStrategy accessStrategy;

    private final NamedParameterJdbcOperations operations;
    private EntityCallbacks entityCallbacks;

    public JdbcExtRepositoryFactory(DataAccessStrategy dataAccessStrategy, RelationalMappingContext context, JdbcConverter converter, Dialect dialect, ApplicationEventPublisher publisher, NamedParameterJdbcOperations operations) {
        super(dataAccessStrategy, context, converter, dialect, publisher, operations);

        this.publisher = publisher;
        this.context = context;
        this.converter = converter;
        this.accessStrategy = dataAccessStrategy;
        this.operations = operations;
    }

    @Override
    protected Object getTargetRepository(RepositoryInformation repositoryInformation) {
        JdbcAggregateTemplate template = new JdbcAggregateTemplate(publisher, context, converter, accessStrategy);

        if (entityCallbacks != null) {
            template.setEntityCallbacks(entityCallbacks);
        }

        RelationalPersistentEntity<?> persistentEntity = context
                .getRequiredPersistentEntity(repositoryInformation.getDomainType());


        return getTargetRepositoryViaReflection(repositoryInformation, operations.getJdbcOperations(), template, persistentEntity,
                converter);
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(Key key, QueryMethodEvaluationContextProvider evaluationContextProvider) {
        Optional<QueryLookupStrategy> original = super.getQueryLookupStrategy(key, evaluationContextProvider);
        if (key == Key.CREATE) {
            return original;
        }

        return Optional.of(new JdbcExtQueryLookupStrategy(operations, context, converter, evaluationContextProvider,
                original.orElseThrow()));
    }

    @Override
    protected Class<?> getRepositoryBaseClass(RepositoryMetadata repositoryMetadata) {
        return JdbcExtDefaultRepository.class;
    }

    @Override
    public void setEntityCallbacks(EntityCallbacks entityCallbacks) {
        this.entityCallbacks = entityCallbacks;
        super.setEntityCallbacks(entityCallbacks);
    }
}
