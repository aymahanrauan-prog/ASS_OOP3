package kz.hotel.repository;

import kz.hotel.domain.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User> {
    Optional<User> findByUsername(String username);
}
