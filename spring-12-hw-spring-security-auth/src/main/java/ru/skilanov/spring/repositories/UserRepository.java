package ru.skilanov.spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skilanov.spring.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
}
