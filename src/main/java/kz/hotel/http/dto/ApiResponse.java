package kz.hotel.http.dto;

public class ApiResponse<T> {
    public boolean success;
    public String message;
    public T data;

    public static <T> ApiResponse<T> ok(String msg, T data) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = true;
        r.message = msg;
        r.data = data;
        return r;
    }

    public static <T> ApiResponse<T> fail(String msg) {
        ApiResponse<T> r = new ApiResponse<>();
        r.success = false;
        r.message = msg;
        r.data = null;
        return r;
    }
}
