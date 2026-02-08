package kz.hotel.api;

import com.sun.net.httpserver.HttpServer;
import kz.hotel.config.AppConfig;
import kz.hotel.service.BookingService;
import kz.hotel.service.GuestService;
import kz.hotel.service.RoomService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class ApiServer {

    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;

    private HttpServer server;

    public ApiServer(RoomService roomService, GuestService guestService, BookingService bookingService) {
        this.roomService = roomService;
        this.guestService = guestService;
        this.bookingService = bookingService;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(AppConfig.API_PORT), 0);
        server.createContext("/api/rooms", new RoomHandler(roomService));
        server.createContext("/api/guests", new GuestHandler(guestService));
        server.createContext("/api/bookings", new BookingHandler(bookingService));
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();
        System.out.println("REST API started: http://localhost:" + AppConfig.API_PORT);
    }

    public void stop() {
        if (server != null) server.stop(0);
    }
}
