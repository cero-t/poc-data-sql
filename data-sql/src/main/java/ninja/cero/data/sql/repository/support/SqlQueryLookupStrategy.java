package ninja.cero.data.sql.repository.support;

import ninja.cero.data.sql.core.convert.RecordMapper;
import ninja.cero.data.sql.repository.query.Query;
import ninja.cero.data.sql.repository.query.SqlFile;
import ninja.cero.data.sql.repository.query.StringQuery;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.stream.Collectors;

public class SqlQueryLookupStrategy implements QueryLookupStrategy {
    private final JdbcTemplate jdbcTemplate;

    public SqlQueryLookupStrategy(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries) {
        QueryMethod queryMethod = new QueryMethod(method, metadata, factory);
        RecordMapper<?> recordMapper = new RecordMapper<>(metadata.getDomainType());

        Query queryAnnotation = method.getAnnotation(Query.class);
        if (queryAnnotation != null) {
            return new StringQuery(jdbcTemplate, queryAnnotation.value(), recordMapper, queryMethod);
        }

        SqlFile sqlFileAnnotation = method.getAnnotation(SqlFile.class);
        if (sqlFileAnnotation != null) {
            return new StringQuery(jdbcTemplate, queryByFile(sqlFileAnnotation), recordMapper, queryMethod);
        }

        throw new RuntimeException("Query cannot be resolved: " + method.getName());
    }

    private String queryByFile(SqlFile sqlFileAnnotation) {
        String fileName = sqlFileAnnotation.value();
        URL resource = getClass().getResource(fileName);

        if (resource == null) {
            throw new RuntimeException("SQL file cannot be read: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
            return reader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new UncheckedIOException("SQL file cannot be read: " + fileName, e);
        }
    }
}
