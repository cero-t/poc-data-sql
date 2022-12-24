package ninja.cero.data.sql.example.app;

import ninja.cero.data.jdbc_ext.repository.JdbcExtRepository;
import ninja.cero.data.jdbc_ext.repository.query.SqlFile;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpRepository extends JdbcExtRepository<Emp, Long> {
    @SqlFile("/sql/selectOdd.sql")
    List<Emp> selectOdd();

    @Query("select * from emp where id in (2, 4, 6)")
    List<Emp> selectEven();
}
