package ninja.cero.data.jdbc_ext.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface JdbcExtRepository <T, ID> extends CrudRepository<T,ID>, PagingAndSortingRepository<T,ID>, QueryByExampleExecutor<T> {
    List<T> query(String query, Object... args);
}
