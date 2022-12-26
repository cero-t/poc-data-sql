package ninja.cero.data.jdbc_ext.repository.query;

import org.springframework.data.annotation.QueryAnnotation;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@QueryAnnotation
@Documented
public @interface SqlFile {
    String value() default "";

    String name() default "";

    Class<? extends RowMapper> rowMapperClass() default RowMapper.class;

    String rowMapperRef() default "";

    Class<? extends ResultSetExtractor> resultSetExtractorClass() default ResultSetExtractor.class;

    String resultSetExtractorRef() default "";
}
