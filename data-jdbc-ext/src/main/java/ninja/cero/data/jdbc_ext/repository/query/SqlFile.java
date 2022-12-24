package ninja.cero.data.jdbc_ext.repository.query;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface SqlFile {
    String value() default "";
}
