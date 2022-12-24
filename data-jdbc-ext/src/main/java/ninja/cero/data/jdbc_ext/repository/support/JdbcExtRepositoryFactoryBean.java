package ninja.cero.data.jdbc_ext.repository.support;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import java.io.Serializable;

public class JdbcExtRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher publisher;
    private BeanFactory beanFactory;
    private RelationalMappingContext mappingContext;
    private JdbcConverter converter;
    private DataAccessStrategy dataAccessStrategy;
    private QueryMappingConfiguration queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
    private NamedParameterJdbcOperations operations;
    private EntityCallbacks entityCallbacks;
    private Dialect dialect;

    public JdbcExtRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        super.setApplicationEventPublisher(publisher);
        this.publisher = publisher;
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        JdbcRepositoryFactory jdbcRepositoryFactory = new JdbcExtRepositoryFactory(dataAccessStrategy,
                mappingContext, converter, dialect, publisher, operations);
        jdbcRepositoryFactory.setQueryMappingConfiguration(queryMappingConfiguration);
        jdbcRepositoryFactory.setEntityCallbacks(entityCallbacks);
        jdbcRepositoryFactory.setBeanFactory(beanFactory);

        return jdbcRepositoryFactory;
    }

    @Autowired
    public void setMappingContext(RelationalMappingContext mappingContext) {
        Assert.notNull(mappingContext, "MappingContext must not be null");
        super.setMappingContext(mappingContext);
        this.mappingContext = mappingContext;
    }

    @Autowired
    public void setDialect(Dialect dialect) {
        Assert.notNull(dialect, "Dialect must not be null");
        this.dialect = dialect;
    }

    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        Assert.notNull(dataAccessStrategy, "DataAccessStrategy must not be null");
        this.dataAccessStrategy = dataAccessStrategy;
    }

    @Autowired(required = false)
    public void setQueryMappingConfiguration(QueryMappingConfiguration queryMappingConfiguration) {
        Assert.notNull(queryMappingConfiguration, "QueryMappingConfiguration must not be null");
        this.queryMappingConfiguration = queryMappingConfiguration;
    }

    public void setJdbcOperations(NamedParameterJdbcOperations operations) {
        Assert.notNull(operations, "NamedParameterJdbcOperations must not be null");
        this.operations = operations;
    }

    @Autowired
    public void setConverter(JdbcConverter converter) {
        Assert.notNull(converter, "JdbcConverter must not be null");
        this.converter = converter;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(this.mappingContext != null, "MappingContext is required and must not be null");
        Assert.state(this.converter != null, "RelationalConverter is required and must not be null");

        if (this.operations == null) {
            Assert.state(beanFactory != null, "If no JdbcOperations are set a BeanFactory must be available");
            this.operations = beanFactory.getBean(NamedParameterJdbcOperations.class);
        }

        if (this.dataAccessStrategy == null) {
            Assert.state(beanFactory != null, "If no DataAccessStrategy is set a BeanFactory must be available");
            this.dataAccessStrategy = this.beanFactory.getBeanProvider(DataAccessStrategy.class) //
                    .getIfAvailable(() -> {
                        Assert.state(this.dialect != null, "Dialect is required and must not be null");
                        SqlGeneratorSource sqlGeneratorSource = new SqlGeneratorSource(this.mappingContext, this.converter,
                                this.dialect);
                        SqlParametersFactory sqlParametersFactory = new SqlParametersFactory(this.mappingContext, this.converter,
                                this.dialect);
                        InsertStrategyFactory insertStrategyFactory = new InsertStrategyFactory(this.operations,
                                new BatchJdbcOperations(this.operations.getJdbcOperations()), this.dialect);
                        return new DefaultDataAccessStrategy(sqlGeneratorSource, this.mappingContext, this.converter,
                                this.operations, sqlParametersFactory, insertStrategyFactory);
                    });
        }

        if (this.queryMappingConfiguration == null) {
            this.queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
        }

        if (beanFactory != null) {
            entityCallbacks = EntityCallbacks.create(beanFactory);
        }

        super.afterPropertiesSet();
    }
}
