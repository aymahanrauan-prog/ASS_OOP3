package kz.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import kz.hotel.domain.Guest;
import kz.hotel.service.GuestService;
import kz.hotel.util.HttpUtil;
import kz.hotel.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class GuestHandler extends BaseHandler {

    private final GuestService guestService;

    public GuestHandler(GuestService guestService) { this.guestService = guestService; }

    @Override
    protected void handleInternal(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            List<Guest> guests = guestService.listGuests();
            HttpUtil.sendJson(ex, 200, JsonUtil.GSON.toJson(guests));
            return;
        }
        HttpUtil.sendText(ex, 405, "Method Not Allowed");
    }
}
