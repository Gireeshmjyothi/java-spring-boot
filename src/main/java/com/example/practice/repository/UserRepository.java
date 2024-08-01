package com.example.practice.repository;
import com.example.practice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    List<User> findAll();

    Optional<User> findByUserName(String userName);

    @Modifying
    @Query("UPDATE User u SET u.loginAttempts = :loginAttempts, u.lastLogin = :currTime WHERE u.userName = :userName")
    void updateLoginAttemptsByUserName(@Param("loginAttempts") long loginAttempts, @Param("currTime") Date currTime, @Param("userName") String userName);
}
