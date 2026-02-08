package kz.hotel.pool;

import kz.hotel.domain.Room;
import kz.hotel.domain.RoomStatus;
import kz.hotel.domain.RoomType;
import kz.hotel.repository.RoomRepository;

import java.util.Comparator;
import java.util.List;

public class RoomDataPool implements DataPool<Room> {

    private final RoomRepository roomRepository;
    private List<Room> cache = List.of();

    public RoomDataPool(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void refresh() { cache = roomRepository.findAll(); }

    @Override
    public List<Room> all() { return cache; }

    public List<Room> freeRooms() {
        return cache.stream().filter(r -> r.getStatus() == RoomStatus.FREE).toList();
    }

    public List<Room> byType(RoomType type) {
        return cache.stream().filter(r -> r.getType() == type).toList();
    }

    public List<Room> sortedByPriceAsc() {
        return cache.stream().sorted(Comparator.comparingInt(Room::getPricePerNight)).toList();
    }
}
