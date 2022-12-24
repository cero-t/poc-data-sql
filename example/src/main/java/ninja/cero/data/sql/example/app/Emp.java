package ninja.cero.data.sql.example.app;

import org.springframework.data.annotation.Id;

public record Emp (@Id Long id, String name) {
}
