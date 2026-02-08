package kz.hotel.console;

import kz.hotel.api.ApiServer;
import kz.hotel.domain.*;
import kz.hotel.exception.AuthException;
import kz.hotel.exception.ValidationException;
import kz.hotel.pool.RoomDataPool;
import kz.hotel.service.*;
import kz.hotel.util.ReflectionPrinter;

import java.time.LocalDate;
import java.util.Scanner;

public class ConsoleMenu {

    private final AuthService authService;
    private final RoomService roomService;
    private final GuestService guestService;
    private final BookingService bookingService;
    private final RoomDataPool roomDataPool;
    private final ApiServer apiServer;

    private User currentUser;

    public ConsoleMenu(AuthService authService,
                       RoomService roomService,
                       GuestService guestService,
                       BookingService bookingService,
                       RoomDataPool roomDataPool,
                       ApiServer apiServer) {
        this.authService = authService;
        this.roomService = roomService;
        this.guestService = guestService;
        this.bookingService = bookingService;
        this.roomDataPool = roomDataPool;
        this.apiServer = apiServer;
    }

    public void run() {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== HotelReservationSystem ===");
        loginLoop(sc);

        while (true) {
            try {
                showMenu();
                String cmd = sc.nextLine().trim();

                if (cmd.equals("0")) {
                    System.out.println("Bye.");
                    return;
                }

                if (cmd.equals("9")) {
                    apiServer.start();
                    continue;
                }

                switch (cmd) {
                    case "1" -> roomsMenu(sc);
                    case "2" -> guestsMenu(sc);
                    case "3" -> bookingsMenu(sc);
                    case "4" -> adminMenu(sc);
                    default -> System.out.println("Unknown command");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private void loginLoop(Scanner sc) {
        while (true) {
            try {
                System.out.print("Username: ");
                String u = sc.nextLine().trim();
                System.out.print("Password: ");
                String p = sc.nextLine().trim();

                currentUser = authService.login(u, p);

                System.out.println("Logged in as: " + currentUser.getRole());
                return;

            } catch (AuthException e) {
                System.out.println("LOGIN FAILED: " + e.getMessage());
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("1) Rooms");
        System.out.println("2) Guests");
        System.out.println("3) Bookings");
        if (currentUser.getRole() == Role.ADMIN) System.out.println("4) Admin");
        System.out.println("9) Start REST API");
        System.out.println("0) Exit");
        System.out.print("> ");
    }

    private void roomsMenu(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("--- ROOMS ---");
            System.out.println("1) List all");
            System.out.println("2) List sorted by price");
            System.out.println("3) List FREE rooms (DataPool)");
            if (currentUser.getRole() == Role.ADMIN) {
                System.out.println("4) Add room");
                System.out.println("5) Delete room");
                System.out.println("6) Mark cleaned (CLEANING->FREE)");
            }
            System.out.println("0) Back");
            System.out.print("> ");

            String cmd = sc.nextLine().trim();
            if (cmd.equals("0")) return;

            switch (cmd) {
                case "1" -> roomService.listRooms().forEach(System.out::println);
                case "2" -> roomService.listRoomsSortedByPriceAsc().forEach(System.out::println);
                case "3" -> {
                    roomDataPool.refresh();
                    roomDataPool.freeRooms().forEach(System.out::println);
                }
                case "4" -> {
                    requireAdmin();
                    System.out.print("Room number: ");
                    String num = sc.nextLine().trim();
                    System.out.print("Type (STANDARD/DELUXE/SUITE): ");
                    RoomType type = RoomType.valueOf(sc.nextLine().trim().toUpperCase());
                    System.out.print("Price per night: ");
                    int price = Integer.parseInt(sc.nextLine().trim());
                    Room r = roomService.addRoom(num, type, price);
                    System.out.println("Created: " + ReflectionPrinter.toPrettyString(r));
                }
                case "5" -> {
                    requireAdmin();
                    System.out.print("Room id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    roomService.deleteRoom(id);
                    System.out.println("Deleted.");
                }
                case "6" -> {
                    requireAdmin();
                    System.out.print("Room id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Room r = bookingService.markCleaned(id);
                    System.out.println("Updated: " + r);
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void guestsMenu(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("--- GUESTS ---");
            System.out.println("1) List all");
            System.out.println("2) List sorted by name");
            System.out.println("3) Add guest");
            if (currentUser.getRole() == Role.ADMIN) System.out.println("4) Delete guest");
            System.out.println("0) Back");
            System.out.print("> ");

            String cmd = sc.nextLine().trim();
            if (cmd.equals("0")) return;

            switch (cmd) {
                case "1" -> guestService.listGuests().forEach(System.out::println);
                case "2" -> guestService.listGuestsSortedByName().forEach(System.out::println);
                case "3" -> {
                    System.out.print("First name: ");
                    String fn = sc.nextLine().trim();
                    System.out.print("Last name: ");
                    String ln = sc.nextLine().trim();
                    System.out.print("Phone: ");
                    String ph = sc.nextLine().trim();
                    System.out.print("Email: ");
                    String em = sc.nextLine().trim();
                    Guest g = guestService.addGuest(fn, ln, ph, em);
                    System.out.println("Created: " + g);
                }
                case "4" -> {
                    requireAdmin();
                    System.out.print("Guest id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    guestService.deleteGuest(id);
                    System.out.println("Deleted.");
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void bookingsMenu(Scanner sc) {
        while (true) {
            System.out.println();
            System.out.println("--- BOOKINGS ---");
            System.out.println("1) List all");
            System.out.println("2) Create booking");
            System.out.println("3) Cancel booking");
            System.out.println("4) Check-in");
            System.out.println("5) Check-out");
            System.out.println("0) Back");
            System.out.print("> ");

            String cmd = sc.nextLine().trim();
            if (cmd.equals("0")) return;

            switch (cmd) {
                case "1" -> bookingService.listBookings().forEach(System.out::println);
                case "2" -> {
                    System.out.print("Room id: ");
                    int roomId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Guest id: ");
                    int guestId = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Check-in (YYYY-MM-DD): ");
                    LocalDate in = LocalDate.parse(sc.nextLine().trim());
                    System.out.print("Check-out (YYYY-MM-DD): ");
                    LocalDate out = LocalDate.parse(sc.nextLine().trim());
                    Booking b = bookingService.createBooking(roomId, guestId, in, out);
                    System.out.println("Created: " + b);
                }
                case "3" -> {
                    System.out.print("Booking id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Booking b = bookingService.cancelBooking(id);
                    System.out.println("Updated: " + b);
                }
                case "4" -> {
                    System.out.print("Booking id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Booking b = bookingService.checkIn(id);
                    System.out.println("Updated: " + b);
                }
                case "5" -> {
                    System.out.print("Booking id: ");
                    int id = Integer.parseInt(sc.nextLine().trim());
                    Booking b = bookingService.checkOut(id);
                    System.out.println("Updated: " + b);
                }
                default -> System.out.println("Unknown command");
            }
        }
    }

    private void adminMenu(Scanner sc) {
        requireAdmin();
        System.out.println();
        System.out.println("--- ADMIN ---");
        System.out.println("Admin menu is minimal. Admin actions are inside other menus.");
        System.out.println("0) Back");
        sc.nextLine();
    }

    private void requireAdmin() {
        if (currentUser.getRole() != Role.ADMIN) throw new ValidationException("Access denied: ADMIN only");
    }
}
