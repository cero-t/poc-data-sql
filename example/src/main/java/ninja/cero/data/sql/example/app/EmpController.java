package ninja.cero.data.sql.example.app;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EmpController {
    EmpRepository empRepository;

    public EmpController(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    @GetMapping("/")
    Iterable<Emp> findAll() {
        return empRepository.findAll();
    }

    @PostMapping("/")
    void insert(@RequestBody Emp emp) {
        empRepository.insert(emp);
    }

    @GetMapping("/{id}")
    Optional<Emp> findById(@PathVariable Long id) {
        return empRepository.findById(id);
    }

    @GetMapping("/odd")
    List<Emp> odd() {
        return empRepository.selectOdd();
    }

    @GetMapping("/even")
    List<Emp> even() {
        return empRepository.selectEven();
    }

    @GetMapping("/search/{name}")
    List<Emp> search(@PathVariable String name) {
        return empRepository.query("select * from emp where name like '%' || ? || '%'", name);
    }
}
