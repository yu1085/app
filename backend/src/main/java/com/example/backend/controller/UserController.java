package com.example.backend.controller;

import com.example.backend.entity.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        User user = userService.login(username, password);
        Map<String, Object> response = new HashMap<>();

        if (user != null) {
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("name", user.getName());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("department", user.getDepartment());
            userInfo.put("status", user.getStatus());
            
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", userInfo);
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String username = registerRequest.get("username");
        String password = registerRequest.get("password");
        String name = registerRequest.get("name");
        String email = registerRequest.get("email");
        String phone = registerRequest.get("phone");

        Map<String, Object> response = new HashMap<>();
        try {
            User user = userService.createUser(username, password, name, email, phone);
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("name", user.getName());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("status", user.getStatus());
            
            response.put("code", 200);
            response.put("message", "注册成功");
            response.put("data", userInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("code", 400);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.ok(response);
        }
    }
} 