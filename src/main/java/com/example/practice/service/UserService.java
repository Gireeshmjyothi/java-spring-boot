package com.example.practice.service;

import com.example.practice.dao.UserDao;
import com.example.practice.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public Optional<User> getUserByUserName(String userName) {
        return userDao.getByUserName(userName);
    }
}
