package ninja.cero.data.sql.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.mapping.DefaultNamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;

public class SqlRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {
    private final RelationalMappingContext mappingContext = new RelationalMappingContext(DefaultNamingStrategy.INSTANCE);

    private JdbcTemplate jdbcTemplate;

    protected SqlRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        return new SqlRepositoryFactory(mappingContext, jdbcTemplate);
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
