package ninja.cero.data.jdbc_ext.repository.query;

import ninja.cero.data.jdbc_ext.spring_data_jdbc.repository.query.JdbcQueryMethod;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class SqlFileQueryMethod extends JdbcQueryMethod {
    private final Method method;

    public SqlFileQueryMethod(Method method, RepositoryMetadata metadata, ProjectionFactory factory, NamedQueries namedQueries, MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> mappingContext) {
        super(method, metadata, factory, namedQueries, mappingContext);
        this.method = method;
    }

    @Override
    protected String getDeclaredQuery() {
        String fileName = (String) this.getMergedAnnotationAttribute("value");
        if (fileName == null) {
            throw new RuntimeException("Annotation value not defined: " + method.getName());
        }

        URL resource = getClass().getResource(fileName);
        if (resource == null) {
            throw new RuntimeException("SQL file not found: " + fileName);
        }

        try (InputStream in = resource.openStream()) {
            return StreamUtils.copyToString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("SQL file cannot be read: " + fileName, e);
        }
    }

    @Override
    public boolean hasAnnotatedQueryName() {
        return this.lookupSqlFileAnnotation()
                .map(SqlFile::name)
                .map(StringUtils::hasText)
                .orElse(false);
    }

    @Override
    @Nullable
    protected Object getMergedAnnotationAttribute(String attribute) {
        SqlFile queryAnnotation = AnnotatedElementUtils.findMergedAnnotation(this.method, SqlFile.class);
        return AnnotationUtils.getValue(queryAnnotation, attribute);
    }

    @Override
    protected Optional<String> findAnnotatedQuery() {
        return this.lookupSqlFileAnnotation()
                .map(SqlFile::value)
                .filter(StringUtils::hasText);
    }

    private Optional<SqlFile> lookupSqlFileAnnotation() {
        return this.doFindAnnotation(SqlFile.class);
    }
}
