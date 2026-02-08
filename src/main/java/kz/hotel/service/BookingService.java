package kz.hotel.service;

import kz.hotel.domain.*;
import kz.hotel.exception.NotFoundException;
import kz.hotel.exception.ValidationException;
import kz.hotel.repository.BookingRepository;
import kz.hotel.repository.GuestRepository;
import kz.hotel.repository.RoomRepository;
import kz.hotel.util.DateUtil;

import java.time.LocalDate;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final GuestRepository guestRepository;

    public BookingService(BookingRepository bookingRepository, RoomRepository roomRepository, GuestRepository guestRepository) {
        this.bookingRepository = bookingRepository;
        this.roomRepository = roomRepository;
        this.guestRepository = guestRepository;
    }

    public Booking createBooking(int roomId, int guestId, LocalDate in, LocalDate out) {
        if (in == null || out == null) throw new ValidationException("Dates cannot be null");
        if (!out.isAfter(in)) throw new ValidationException("checkOut must be after checkIn");

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found: id=" + roomId));

        guestRepository.findById(guestId)
                .orElseThrow(() -> new NotFoundException("Guest not found: id=" + guestId));

        if (bookingRepository.hasOverlappingBooking(roomId, in, out)) {
            throw new ValidationException("Room already booked for these dates");
        }

        long nights = DateUtil.nights(in, out);
        int total = (int) (nights * room.getPricePerNight());

        Booking b = new Booking.Builder()
                .roomId(roomId)
                .guestId(guestId)
                .checkIn(in)
                .checkOut(out)
                .totalPrice(total)
                .status(BookingStatus.BOOKED)
                .build();

        Booking created = bookingRepository.create(b);

        if (room.getStatus() == RoomStatus.FREE) {
            room.setStatus(RoomStatus.RESERVED);
            roomRepository.update(room);
        }

        return created;
    }

    public List<Booking> listBookings() { return bookingRepository.findAll(); }

    public Booking getBooking(int id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Booking not found: id=" + id));
    }

    public Booking cancelBooking(int id) {
        Booking b = getBooking(id);
        if (b.getStatus() == BookingStatus.COMPLETED) throw new ValidationException("Cannot cancel completed booking");
        if (b.getStatus() == BookingStatus.CANCELED) return b;

        b.setStatus(BookingStatus.CANCELED);
        Booking updated = bookingRepository.update(b);

        Room room = roomRepository.findById(b.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found: id=" + b.getRoomId()));

        boolean stillHas = bookingRepository.findByRoomId(room.getId()).stream()
                .anyMatch(x -> x.getStatus() == BookingStatus.BOOKED || x.getStatus() == BookingStatus.CHECKED_IN);

        if (!stillHas && room.getStatus() == RoomStatus.RESERVED) {
            room.setStatus(RoomStatus.FREE);
            roomRepository.update(room);
        }
        return updated;
    }

    public Booking checkIn(int bookingId) {
        Booking b = getBooking(bookingId);
        if (b.getStatus() != BookingStatus.BOOKED) throw new ValidationException("Check-in only for BOOKED");

        Room room = roomRepository.findById(b.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found: id=" + b.getRoomId()));

        b.setStatus(BookingStatus.CHECKED_IN);
        Booking updated = bookingRepository.update(b);

        room.setStatus(RoomStatus.BUSY);
        roomRepository.update(room);

        return updated;
    }

    public Booking checkOut(int bookingId) {
        Booking b = getBooking(bookingId);
        if (b.getStatus() != BookingStatus.CHECKED_IN) throw new ValidationException("Check-out only for CHECKED_IN");

        Room room = roomRepository.findById(b.getRoomId())
                .orElseThrow(() -> new NotFoundException("Room not found: id=" + b.getRoomId()));

        b.setStatus(BookingStatus.COMPLETED);
        Booking updated = bookingRepository.update(b);

        room.setStatus(RoomStatus.CLEANING);
        roomRepository.update(room);

        return updated;
    }

    public Room markCleaned(int roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("Room not found: id=" + roomId));

        if (room.getStatus() != RoomStatus.CLEANING) throw new ValidationException("Room must be CLEANING");
        room.setStatus(RoomStatus.FREE);
        return roomRepository.update(room);
    }
}
