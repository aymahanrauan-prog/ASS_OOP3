package kz.hotel.factory;

import kz.hotel.api.ApiServer;
import kz.hotel.pool.RoomDataPool;
import kz.hotel.repository.*;
import kz.hotel.repository.jdbc.*;
import kz.hotel.service.*;

public final class AppFactory {
    private AppFactory() {}

    public static UserRepository userRepository() { return new JdbcUserRepository(); }
    public static RoomRepository roomRepository() { return new JdbcRoomRepository(); }
    public static GuestRepository guestRepository() { return new JdbcGuestRepository(); }
    public static BookingRepository bookingRepository() { return new JdbcBookingRepository(); }

    public static AuthService authService() { return new AuthService(userRepository()); }
    public static RoomService roomService() { return new RoomService(roomRepository()); }
    public static GuestService guestService() { return new GuestService(guestRepository()); }
    public static BookingService bookingService() {
        return new BookingService(bookingRepository(), roomRepository(), guestRepository());
    }

    public static RoomDataPool roomDataPool() { return new RoomDataPool(roomRepository()); }

    public static ApiServer apiServer() {
        return new ApiServer(roomService(), guestService(), bookingService());
    }
}
