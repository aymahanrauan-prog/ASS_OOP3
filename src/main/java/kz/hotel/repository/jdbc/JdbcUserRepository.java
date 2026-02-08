package kz.hotel.repository.jdbc;

import kz.hotel.db.DB;
import kz.hotel.domain.Role;
import kz.hotel.domain.User;
import kz.hotel.repository.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcUserRepository implements UserRepository {

    @Override
    public User create(User u) {
        String sql = "INSERT INTO users(username, password_hash, role, active) VALUES(?,?,?,?) RETURNING id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRole().name());
            ps.setBoolean(4, u.isActive());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return new User(rs.getInt("id"), u.getUsername(), u.getPasswordHash(), u.getRole(), u.isActive());
            throw new SQLException("No id returned");
        } catch (SQLException e) {
            throw new RuntimeException("DB error create user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "SELECT * FROM users WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("DB error find user by username: " + e.getMessage(), e);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users ORDER BY id";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            List<User> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("DB error list users: " + e.getMessage(), e);
        }
    }

    @Override
    public User update(User u) {
        String sql = "UPDATE users SET username=?, password_hash=?, role=?, active=? WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPasswordHash());
            ps.setString(3, u.getRole().name());
            ps.setBoolean(4, u.isActive());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
            return u;
        } catch (SQLException e) {
            throw new RuntimeException("DB error update user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("DB error delete user: " + e.getMessage(), e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password_hash"),
                Role.valueOf(rs.getString("role")), rs.getBoolean("active"));
    }
}
