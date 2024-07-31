package com.example.practice.repository;
import com.example.practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAll();

    Optional<User> findByUserName(String userName);
}
