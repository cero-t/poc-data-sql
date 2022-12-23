package ninja.cero.data.sql.example.app;

import ninja.cero.data.sql.repository.SqlRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends SqlRepository<Item, Long> {
}
