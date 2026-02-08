package kz.hotel.repository.jdbc;

import kz.hotel.db.DB;
import kz.hotel.domain.Room;
import kz.hotel.domain.RoomStatus;
import kz.hotel.domain.RoomType;
import kz.hotel.repository.RoomRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcRoomRepository implements RoomRepository {

    @Override
    public Room create(Room r) {
        String sql = "INSERT INTO rooms(room_number, type, price_per_night, status) VALUES(?,?,?,?) RETURNING id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getType().name());
            ps.setInt(3, r.getPricePerNight());
            ps.setString(4, r.getStatus().name());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Room(rs.getInt("id"), r.getRoomNumber(), r.getType(), r.getPricePerNight(), r.getStatus());
            throw new SQLException("No id returned");
        } catch (SQLException e) {
            throw new RuntimeException("DB error create room: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Room> findById(int id) {
        String sql = "SELECT * FROM rooms WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find room: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Room> findAll() {
        String sql = "SELECT * FROM rooms ORDER BY id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Room> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB error list rooms: " + e.getMessage(), e);
        }
    }

    @Override
    public Room update(Room r) {
        String sql = "UPDATE rooms SET room_number=?, type=?, price_per_night=?, status=? WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, r.getRoomNumber());
            ps.setString(2, r.getType().name());
            ps.setInt(3, r.getPricePerNight());
            ps.setString(4, r.getStatus().name());
            ps.setInt(5, r.getId());
            ps.executeUpdate();
            return r;
        } catch (SQLException e) {
            throw new RuntimeException("DB error update room: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM rooms WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB error delete room: " + e.getMessage(), e);
        }
    }

    private Room map(ResultSet rs) throws SQLException {
        return new Room(rs.getInt("id"), rs.getString("room_number"),
                RoomType.valueOf(rs.getString("type")),
                rs.getInt("price_per_night"),
                RoomStatus.valueOf(rs.getString("status")));
    }
}
