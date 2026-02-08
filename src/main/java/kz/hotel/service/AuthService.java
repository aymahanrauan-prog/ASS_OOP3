package kz.hotel.service;

import kz.hotel.domain.User;
import kz.hotel.exception.AuthException;
import kz.hotel.repository.UserRepository;
import kz.hotel.util.HashUtil;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) {
        User u = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthException("Wrong username or password"));

        if (!u.isActive()) throw new AuthException("User is disabled");

        String hash = HashUtil.sha256(password);
        if (!hash.equals(u.getPasswordHash())) throw new AuthException("Wrong username or password");
        return u;
    }
}
