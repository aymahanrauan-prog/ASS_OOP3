package kz.hotel.repository;

import kz.hotel.domain.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends CrudRepository<Booking> {
    List<Booking> findByRoomId(int roomId);
    boolean hasOverlappingBooking(int roomId, LocalDate in, LocalDate out);
}
