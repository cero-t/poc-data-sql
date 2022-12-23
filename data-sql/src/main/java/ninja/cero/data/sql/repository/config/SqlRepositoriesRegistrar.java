package ninja.cero.data.sql.repository.config;

import org.springframework.data.repository.config.RepositoryBeanDefinitionRegistrarSupport;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

public class SqlRepositoriesRegistrar extends RepositoryBeanDefinitionRegistrarSupport {
    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableSqlRepositories.class;
    }

    @Override
    protected RepositoryConfigurationExtension getExtension() {
        return new SqlRepositoryConfigExtension();
    }
}
