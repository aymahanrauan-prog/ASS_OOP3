package kz.hotel.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    T create(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    T update(T entity);
    boolean delete(int id);
}
