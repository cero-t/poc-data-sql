package ninja.cero.data.sql.example.app;

import org.springframework.data.annotation.Id;

import java.sql.Date;

public record Item(@Id Long id,
                   String name,
                   String media,
                   String author,
                   Long unitPrice,
                   Date release,
                   String image) {
}
