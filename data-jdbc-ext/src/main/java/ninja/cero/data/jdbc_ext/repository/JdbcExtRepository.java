package ninja.cero.data.jdbc_ext.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;

import java.util.List;

public interface JdbcExtRepository<T, ID> extends ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, ListQueryByExampleExecutor<T> {
    List<T> query(String query, Object... args);

    <S extends T> S insert(S entity);
}
