package kz.hotel.repository.jdbc;

import kz.hotel.db.DB;
import kz.hotel.domain.Booking;
import kz.hotel.domain.BookingStatus;
import kz.hotel.repository.BookingRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcBookingRepository implements BookingRepository {

    @Override
    public Booking create(Booking b) {
        String sql = "INSERT INTO bookings(room_id, guest_id, check_in, check_out, total_price, status) VALUES(?,?,?,?,?,?) RETURNING id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getRoomId());
            ps.setInt(2, b.getGuestId());
            ps.setDate(3, Date.valueOf(b.getCheckIn()));
            ps.setDate(4, Date.valueOf(b.getCheckOut()));
            if (b.getTotalPrice() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, b.getTotalPrice());
            ps.setString(6, b.getStatus().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Booking.Builder()
                        .id(rs.getInt("id"))
                        .roomId(b.getRoomId())
                        .guestId(b.getGuestId())
                        .checkIn(b.getCheckIn())
                        .checkOut(b.getCheckOut())
                        .totalPrice(b.getTotalPrice())
                        .status(b.getStatus())
                        .build();
            }
            throw new SQLException("No id returned");
        } catch (SQLException e) {
            throw new RuntimeException("DB error create booking: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Booking> findById(int id) {
        String sql = "SELECT * FROM bookings WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT * FROM bookings ORDER BY id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Booking> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB error list bookings: " + e.getMessage(), e);
        }
    }

    @Override
    public Booking update(Booking b) {
        String sql = "UPDATE bookings SET room_id=?, guest_id=?, check_in=?, check_out=?, total_price=?, status=? WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, b.getRoomId());
            ps.setInt(2, b.getGuestId());
            ps.setDate(3, Date.valueOf(b.getCheckIn()));
            ps.setDate(4, Date.valueOf(b.getCheckOut()));
            if (b.getTotalPrice() == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, b.getTotalPrice());
            ps.setString(6, b.getStatus().name());
            ps.setInt(7, b.getId());
            ps.executeUpdate();
            return b;
        } catch (SQLException e) {
            throw new RuntimeException("DB error update booking: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM bookings WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB error delete booking: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Booking> findByRoomId(int roomId) {
        String sql = "SELECT * FROM bookings WHERE room_id=? ORDER BY id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            List<Booking> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB error list bookings by room: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean hasOverlappingBooking(int roomId, LocalDate in, LocalDate out) {
        String sql = "SELECT 1 FROM bookings WHERE room_id=? AND status IN ('BOOKED','CHECKED_IN') AND ? < check_out AND ? > check_in LIMIT 1";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setDate(2, Date.valueOf(in));
            ps.setDate(3, Date.valueOf(out));
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException("DB error overlap check: " + e.getMessage(), e);
        }
    }

    private Booking map(ResultSet rs) throws SQLException {
        Integer total = rs.getObject("total_price") == null ? null : rs.getInt("total_price");
        return new Booking.Builder()
                .id(rs.getInt("id"))
                .roomId(rs.getInt("room_id"))
                .guestId(rs.getInt("guest_id"))
                .checkIn(rs.getDate("check_in").toLocalDate())
                .checkOut(rs.getDate("check_out").toLocalDate())
                .totalPrice(total)
                .status(BookingStatus.valueOf(rs.getString("status")))
                .build();
    }
}
