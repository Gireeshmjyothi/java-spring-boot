package com.example.practice.dao;


import com.example.practice.entity.User;
import com.example.practice.repository.UserRepository;
import com.example.practice.util.DateUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDao {
    private final UserRepository userRepository;

    public List<User> getUserData(){
        return userRepository.findAll();
    }

    public Optional<User> getByUserName(String userName) {
        log.info("Fetching user details by userName {}:", userName);
        return userRepository.findByUserName(userName);
    }

    @Transactional
    public void updateLoginAttempt(User user, boolean loginStatus) {
        log.debug("requesting to update LoginAttempt");
        if (loginStatus) {
            userRepository.updateLoginAttemptsByUserName(0, DateUtil.getDate(), user.getUsername());
        }
    }
}
