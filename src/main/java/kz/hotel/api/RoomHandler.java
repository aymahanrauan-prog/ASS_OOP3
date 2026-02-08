package kz.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import kz.hotel.domain.Room;
import kz.hotel.service.RoomService;
import kz.hotel.util.HttpUtil;
import kz.hotel.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class RoomHandler extends BaseHandler {

    private final RoomService roomService;

    public RoomHandler(RoomService roomService) { this.roomService = roomService; }

    @Override
    protected void handleInternal(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            List<Room> rooms = roomService.listRooms();
            HttpUtil.sendJson(ex, 200, JsonUtil.GSON.toJson(rooms));
            return;
        }
        HttpUtil.sendText(ex, 405, "Method Not Allowed");
    }
}
