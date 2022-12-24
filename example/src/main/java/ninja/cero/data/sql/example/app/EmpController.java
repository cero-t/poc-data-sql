package ninja.cero.data.sql.example.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmpController {
    EmpRepository empRepository;

    public EmpController(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    @GetMapping("/")
    List<Emp> emp() {
        return empRepository.findAll();
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
