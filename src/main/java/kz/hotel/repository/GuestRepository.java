package kz.hotel.repository;

import kz.hotel.domain.Guest;
import java.util.Optional;

public interface GuestRepository extends CrudRepository<Guest> {
    Optional<Guest> findByPhone(String phone);
}
