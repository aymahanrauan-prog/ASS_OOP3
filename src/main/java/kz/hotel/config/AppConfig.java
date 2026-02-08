package kz.hotel.config;

public final class AppConfig {
    private AppConfig() {}

    public static final String DB_URL = "jdbc:postgresql://localhost:5432/hotel_db";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "rauan328";

    public static final int API_PORT = 8081;
}
