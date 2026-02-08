package kz.hotel.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import kz.hotel.util.HttpUtil;

import java.io.IOException;

public abstract class BaseHandler implements HttpHandler {

    @Override
    public final void handle(HttpExchange ex) throws IOException {
        try {
            handleInternal(ex);
        } catch (Exception e) {
            HttpUtil.sendText(ex, 400, "ERROR: " + e.getMessage());
        }
    }

    protected abstract void handleInternal(HttpExchange ex) throws IOException;
}
