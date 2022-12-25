package ninja.cero.data.sql.example;

import ninja.cero.data.jdbc_ext.repository.support.JdbcExtRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@SpringBootApplication
@EnableJdbcRepositories(repositoryFactoryBeanClass = JdbcExtRepositoryFactoryBean.class)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
