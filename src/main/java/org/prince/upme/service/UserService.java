package org.prince.upme.service;

import org.prince.upme.model.User;

import java.util.Optional;

public interface UserService {
    void registerUser(User newUser);
    Optional<User> findByEmail(String email);
}
