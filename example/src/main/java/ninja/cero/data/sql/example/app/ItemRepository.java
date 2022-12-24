package ninja.cero.data.sql.example.app;

import ninja.cero.data.jdbc_ext.repository.JdbcExtRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JdbcExtRepository<Item, Long> {
}
