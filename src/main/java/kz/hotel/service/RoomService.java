package kz.hotel.service;

import kz.hotel.domain.Room;
import kz.hotel.domain.RoomStatus;
import kz.hotel.domain.RoomType;
import kz.hotel.exception.NotFoundException;
import kz.hotel.exception.ValidationException;
import kz.hotel.repository.RoomRepository;

import java.util.Comparator;
import java.util.List;

public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room addRoom(String roomNumber, RoomType type, int pricePerNight) {
        if (roomNumber == null || roomNumber.isBlank()) throw new ValidationException("Room number is empty");
        if (pricePerNight <= 0) throw new ValidationException("Price must be > 0");
        return roomRepository.create(new Room(0, roomNumber, type, pricePerNight, RoomStatus.FREE));
    }

    public List<Room> listRooms() { return roomRepository.findAll(); }

    public List<Room> listRoomsSortedByPriceAsc() {
        return roomRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Room::getPricePerNight))
                .toList();
    }

    public Room getRoom(int id) {
        return roomRepository.findById(id).orElseThrow(() -> new NotFoundException("Room not found: id=" + id));
    }

    public void deleteRoom(int id) {
        Room r = getRoom(id);
        if (r.getStatus() == RoomStatus.BUSY || r.getStatus() == RoomStatus.RESERVED) {
            throw new ValidationException("Cannot delete room while status is " + r.getStatus());
        }
        if (!roomRepository.delete(id)) throw new NotFoundException("Room not found: id=" + id);
    }

    public Room setRoomStatus(int id, RoomStatus status) {
        Room r = getRoom(id);
        r.setStatus(status);
        return roomRepository.update(r);
    }
}
