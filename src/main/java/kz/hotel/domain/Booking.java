package kz.hotel.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Booking {

    private int id;
    private int roomId;
    private int guestId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Integer totalPrice;
    private BookingStatus status;

    private Booking(Builder b) {
        this.id = b.id;
        this.roomId = b.roomId;
        this.guestId = b.guestId;
        this.checkIn = b.checkIn;
        this.checkOut = b.checkOut;
        this.totalPrice = b.totalPrice;
        this.status = b.status;
    }

    public int getId() { return id; }
    public int getRoomId() { return roomId; }
    public int getGuestId() { return guestId; }
    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public Integer getTotalPrice() { return totalPrice; }
    public BookingStatus getStatus() { return status; }

    public void setStatus(BookingStatus status) { this.status = status; }
    public void setTotalPrice(Integer totalPrice) { this.totalPrice = totalPrice; }

    @Override
    public String toString() {
        return "Booking{id=" + id +
                ", roomId=" + roomId +
                ", guestId=" + guestId +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        Booking booking = (Booking) o;
        return id == booking.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static class Builder {
        private int id;
        private int roomId;
        private int guestId;
        private LocalDate checkIn;
        private LocalDate checkOut;
        private Integer totalPrice;
        private BookingStatus status = BookingStatus.BOOKED;

        public Builder id(int id) { this.id = id; return this; }
        public Builder roomId(int roomId) { this.roomId = roomId; return this; }
        public Builder guestId(int guestId) { this.guestId = guestId; return this; }
        public Builder checkIn(LocalDate checkIn) { this.checkIn = checkIn; return this; }
        public Builder checkOut(LocalDate checkOut) { this.checkOut = checkOut; return this; }
        public Builder totalPrice(Integer totalPrice) { this.totalPrice = totalPrice; return this; }
        public Builder status(BookingStatus status) { this.status = status; return this; }

        public Booking build() { return new Booking(this); }
    }
}
