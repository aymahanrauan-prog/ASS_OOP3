package kz.hotel.http;

import com.sun.net.httpserver.HttpServer;
import kz.hotel.http.dto.ApiResponse;
import kz.hotel.http.dto.LoginRequest;
import kz.hotel.http.util.HttpUtil;
import kz.hotel.http.util.JsonUtil;
import kz.hotel.domain.User;
import kz.hotel.service.AuthService;
import kz.hotel.service.RoomService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class RestServer {

    private final int port;
    private HttpServer server;

    private final AuthService authService;
    private final RoomService roomService;

    public RestServer(int port, AuthService authService, RoomService roomService) {
        this.port = port;
        this.authService = authService;
        this.roomService = roomService;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);

        // TEST ROOT
        server.createContext("/", exchange -> {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                HttpUtil.sendText(exchange, 405, "Method Not Allowed");
                return;
            }
            HttpUtil.sendText(exchange, 200,
                    "HotelReservationSystem REST API is running.\n" +
                            "Try: GET /api/rooms\n");
        });

        // LOGIN
        server.createContext("/api/login", exchange -> {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    HttpUtil.sendJson(exchange, 405, JsonUtil.toJson(ApiResponse.fail("Method Not Allowed")));
                    return;
                }

                String body = HttpUtil.readBody(exchange);
                LoginRequest req = JsonUtil.fromJson(body, LoginRequest.class);

                if (req == null || req.username == null || req.password == null) {
                    HttpUtil.sendJson(exchange, 400, JsonUtil.toJson(ApiResponse.fail("Bad JSON")));
                    return;
                }

                User user = authService.login(req.username, req.password);

                // ⚠️ Тут по-хорошему нужен token/session.
                // Но для учебного проекта хватит просто вернуть user.
                HttpUtil.sendJson(exchange, 200, JsonUtil.toJson(ApiResponse.ok("Login success", user)));

            } catch (Exception e) {
                HttpUtil.sendJson(exchange, 401, JsonUtil.toJson(ApiResponse.fail("Login failed: " + e.getMessage())));
            }
        });

        // ROOMS LIST
        server.createContext("/api/rooms", exchange -> {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    HttpUtil.sendJson(exchange, 405, JsonUtil.toJson(ApiResponse.fail("Method Not Allowed")));
                    return;
                }

                List<?> rooms = roomService.listRooms();
                HttpUtil.sendJson(exchange, 200, JsonUtil.toJson(ApiResponse.ok("Rooms list", rooms)));

            } catch (Exception e) {
                HttpUtil.sendJson(exchange, 500, JsonUtil.toJson(ApiResponse.fail("Server error: " + e.getMessage())));
            }
        });

        server.setExecutor(null);
        server.start();

        System.out.println("REST API started: http://localhost:" + port);
    }

    public void stop() {
        if (server != null) server.stop(0);
    }
}
