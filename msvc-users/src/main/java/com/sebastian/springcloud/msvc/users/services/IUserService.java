package com.sebastian.springcloud.msvc.users.services;

import java.util.List;
import java.util.Optional;

import com.sebastian.springcloud.msvc.users.entities.User;

public interface IUserService {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> update(User user, Long id);

    User save(User user);

    void deleteById(Long id);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
