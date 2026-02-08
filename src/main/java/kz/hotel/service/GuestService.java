package kz.hotel.service;

import kz.hotel.domain.Guest;
import kz.hotel.exception.NotFoundException;
import kz.hotel.exception.ValidationException;
import kz.hotel.repository.GuestRepository;

import java.util.Comparator;
import java.util.List;

public class GuestService {

    private final GuestRepository guestRepository;

    public GuestService(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    public Guest addGuest(String firstName, String lastName, String phone, String email) {
        if (firstName == null || firstName.isBlank()) throw new ValidationException("First name is empty");
        if (lastName == null || lastName.isBlank()) throw new ValidationException("Last name is empty");
        return guestRepository.create(new Guest(0, firstName, lastName, phone, email));
    }

    public List<Guest> listGuests() { return guestRepository.findAll(); }

    public List<Guest> listGuestsSortedByName() {
        return guestRepository.findAll().stream()
                .sorted(Comparator.comparing(Guest::getFullName))
                .toList();
    }

    public Guest getGuest(int id) {
        return guestRepository.findById(id).orElseThrow(() -> new NotFoundException("Guest not found: id=" + id));
    }

    public void deleteGuest(int id) {
        if (!guestRepository.delete(id)) throw new NotFoundException("Guest not found: id=" + id);
    }
}
