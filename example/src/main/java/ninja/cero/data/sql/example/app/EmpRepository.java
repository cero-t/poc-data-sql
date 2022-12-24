package ninja.cero.data.sql.example.app;

import ninja.cero.data.sql.repository.SqlRepository;
import ninja.cero.data.sql.repository.query.Query;
import ninja.cero.data.sql.repository.query.SqlFile;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpRepository extends SqlRepository<Emp, Long> {
    @SqlFile("/sql/selectOdd.sql")
    List<Emp> selectOdd();

    @Query("select * from emp where id in (2, 4, 6)")
    List<Emp> selectEven();
}
