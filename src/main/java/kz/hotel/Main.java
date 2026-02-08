package kz.hotel;

import kz.hotel.console.ConsoleMenu;
import kz.hotel.repository.jdbc.*;
import kz.hotel.service.*;
import kz.hotel.factory.AppFactory;


public class Main {
    public static void main(String[] args) {

        ConsoleMenu menu = new ConsoleMenu(
                AppFactory.authService(),
                AppFactory.roomService(),
                AppFactory.guestService(),
                AppFactory.bookingService(),
                AppFactory.roomDataPool(),
                AppFactory.apiServer()
        );
        menu.run();
    }
}