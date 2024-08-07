package ru.skilanov.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skilanov.spring.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
}
