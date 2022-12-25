package ninja.cero.data.jdbc_ext.repository.support;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.support.SimpleJdbcRepository;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.ListQueryByExampleExecutor;

import java.util.ArrayList;
import java.util.List;

public class ListSimpleJdbcRepository<T, ID> extends SimpleJdbcRepository<T, ID>
        implements ListCrudRepository<T, ID>, ListPagingAndSortingRepository<T, ID>, ListQueryByExampleExecutor<T> {
    public ListSimpleJdbcRepository(JdbcAggregateOperations entityOperations, PersistentEntity<T, ?> entity, JdbcConverter converter) {
        super(entityOperations, entity, converter);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return toList(super.saveAll(entities));
    }

    @Override
    public List<T> findAll() {
        return toList(super.findAll());
    }

    @Override
    public List<T> findAllById(Iterable<ID> ids) {
        return toList(super.findAllById(ids));
    }

    @Override
    public List<T> findAll(Sort sort) {
        return toList(super.findAll(sort));
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return toList(super.findAll(example));
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return toList(super.findAll(example, sort));
    }

    private <X> List<X> toList(Iterable<X> iterable) {
        if (iterable instanceof List<X> list) {
            return list;
        }

        List<X> list = new ArrayList<>();
        iterable.forEach(list::add);

        return list;
    }
}
