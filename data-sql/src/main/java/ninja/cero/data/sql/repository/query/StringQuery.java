package ninja.cero.data.sql.repository.query;

import ninja.cero.data.sql.core.convert.RecordMapper;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.jdbc.core.JdbcTemplate;

public class StringQuery implements RepositoryQuery {
    private JdbcTemplate jdbcTemplate;
    private String query;

    private RecordMapper<?> recordMapper;

    private QueryMethod queryMethod;

    public StringQuery(JdbcTemplate jdbcTemplate, String query, RecordMapper<?> recordMapper, QueryMethod queryMethod) {
        this.jdbcTemplate = jdbcTemplate;
        this.query = query;
        this.recordMapper = recordMapper;
        this.queryMethod = queryMethod;
    }

    @Override
    public Object execute(Object[] parameters) {
        return jdbcTemplate.query(query, recordMapper);
    }

    @Override
    public QueryMethod getQueryMethod() {
        return queryMethod;
    }
}
