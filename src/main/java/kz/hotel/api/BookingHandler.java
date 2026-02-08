package kz.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import kz.hotel.domain.Booking;
import kz.hotel.service.BookingService;
import kz.hotel.util.HttpUtil;
import kz.hotel.util.JsonUtil;

import java.io.IOException;
import java.util.List;

public class BookingHandler extends BaseHandler {

    private final BookingService bookingService;

    public BookingHandler(BookingService bookingService) { this.bookingService = bookingService; }

    @Override
    protected void handleInternal(HttpExchange ex) throws IOException {
        if (ex.getRequestMethod().equalsIgnoreCase("GET")) {
            List<Booking> bookings = bookingService.listBookings();
            HttpUtil.sendJson(ex, 200, JsonUtil.GSON.toJson(bookings));
            return;
        }
        HttpUtil.sendText(ex, 405, "Method Not Allowed");
    }
}
