package ninja.cero.data.jdbc_ext.repository.query;

import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URL;
import java.util.stream.Collectors;

public class SqlFileQuery implements RepositoryQuery {
    private SqlFile annotation;

    private JdbcOperations jdbcOperations;

    private RowMapper<?> recordMapper;

    private QueryMethod queryMethod;

    public SqlFileQuery(SqlFile annotation, JdbcOperations jdbcOperations, RowMapper<?> recordMapper, QueryMethod queryMethod) {
        this.annotation = annotation;
        this.jdbcOperations = jdbcOperations;
        this.recordMapper = recordMapper;
        this.queryMethod = queryMethod;
    }

    @Override
    public Object execute(Object[] parameters) {
        return jdbcOperations.query(getQuery(), recordMapper);
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }

    private String getQuery() {
        String fileName = annotation.value();
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
