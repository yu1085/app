package com.example.backend.service;

import com.example.backend.entity.User;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;

    public User login(String username, String password) {
        logger.info("Attempting login for user: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("User not found: {}", username);
            return null;
        }
        logger.info("Found user: {}, stored password: {}", username, user.getPassword());
        boolean matches = password.equals(user.getPassword());
        logger.info("Password matches: {}", matches);
        if (matches) {
            return user;
        }
        return null;
    }

    public User createUser(String username, String password, String name, String email, String phone) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(username) != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        return userRepository.save(user);
    }
} 