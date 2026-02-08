package kz.hotel.domain;

import java.util.Objects;

public class Room {

    private int id;
    private String roomNumber;
    private RoomType type;
    private int pricePerNight;
    private RoomStatus status;

    public Room(int id, String roomNumber, RoomType type, int pricePerNight, RoomStatus status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public int getId() { return id; }
    public String getRoomNumber() { return roomNumber; }
    public RoomType getType() { return type; }
    public int getPricePerNight() { return pricePerNight; }
    public RoomStatus getStatus() { return status; }
    public void setStatus(RoomStatus status) { this.status = status; }

    @Override
    public String toString() {
        return "Room{id=" + id +
                ", roomNumber='" + roomNumber +
                ", type=" + type +
                ", pricePerNight=" + pricePerNight +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id == room.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
