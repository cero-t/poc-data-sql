package ninja.cero.data.sql.repository.config;

import ninja.cero.data.sql.repository.support.SqlRepositoryFactoryBean;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;

public class SqlRepositoryConfigExtension extends RepositoryConfigurationExtensionSupport {
    @Override
    protected String getModulePrefix() {
        return "sql";
    }

    @Override
    public String getRepositoryFactoryBeanClassName() {
        return SqlRepositoryFactoryBean.class.getName();
    }
}
