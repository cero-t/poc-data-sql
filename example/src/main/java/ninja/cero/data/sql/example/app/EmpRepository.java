package ninja.cero.data.sql.example.app;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpRepository extends CrudRepository<Emp, Long> {
}