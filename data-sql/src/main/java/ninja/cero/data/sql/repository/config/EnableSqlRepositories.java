package ninja.cero.data.sql.repository.config;

import ninja.cero.data.sql.repository.support.SqlRepositoryFactoryBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(SqlRepositoriesRegistrar.class)
public @interface EnableSqlRepositories {
    String[] value() default {};
    String[] basePackages() default {};
    Class<?>[] basePackageClasses() default {};
    ComponentScan.Filter[] includeFilters() default {};
    ComponentScan.Filter[] excludeFilters() default {};
    String repositoryImplementationPostfix() default "Impl";
    String namedQueriesLocation() default "";
    Class<?> repositoryFactoryBeanClass() default SqlRepositoryFactoryBean.class;
}
