package kz.hotel.repository.jdbc;

import kz.hotel.db.DB;
import kz.hotel.domain.Guest;
import kz.hotel.repository.GuestRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcGuestRepository implements GuestRepository {

    @Override
    public Guest create(Guest g) {
        String sql = "INSERT INTO guests(first_name, last_name, phone, email) VALUES(?,?,?,?) RETURNING id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getFirstName());
            ps.setString(2, g.getLastName());
            ps.setString(3, g.getPhone());
            ps.setString(4, g.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new Guest(rs.getInt("id"), g.getFirstName(), g.getLastName(), g.getPhone(), g.getEmail());
            throw new SQLException("No id returned");
        } catch (SQLException e) {
            throw new RuntimeException("DB error create guest: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Guest> findById(int id) {
        String sql = "SELECT * FROM guests WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find guest: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Guest> findByPhone(String phone) {
        String sql = "SELECT * FROM guests WHERE phone=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find guest by phone: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Guest> findAll() {
        String sql = "SELECT * FROM guests ORDER BY id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<Guest> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB error list guests: " + e.getMessage(), e);
        }
    }

    @Override
    public Guest update(Guest g) {
        String sql = "UPDATE guests SET first_name=?, last_name=?, phone=?, email=? WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, g.getFirstName());
            ps.setString(2, g.getLastName());
            ps.setString(3, g.getPhone());
            ps.setString(4, g.getEmail());
            ps.setInt(5, g.getId());
            ps.executeUpdate();
            return g;
        } catch (SQLException e) {
            throw new RuntimeException("DB error update guest: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM guests WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB error delete guest: " + e.getMessage(), e);
        }
    }

    private Guest map(ResultSet rs) throws SQLException {
        return new Guest(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
                rs.getString("phone"), rs.getString("email"));
    }
}
