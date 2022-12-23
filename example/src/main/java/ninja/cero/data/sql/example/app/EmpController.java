package ninja.cero.data.sql.example.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmpController {
    EmpRepository empRepository;

    public EmpController(EmpRepository empRepository) {
        this.empRepository = empRepository;
    }

    @GetMapping("/")
    Iterable<Emp> emp() {
        return empRepository.findAll();
    }
}