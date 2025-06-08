package com.tinyhouse.controller;

import com.tinyhouse.model.User;
import com.tinyhouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tinyhouse.model.LoginRequest;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // E-posta daha önce kullanılmış mı?
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu e-posta zaten kayıtlı.");
        }

        // Hesabı aktif olarak işaretle
        user.setIsActive(true);

        // Kullanıcıyı kaydet
        userRepository.save(user);

        return ResponseEntity.ok("Kayıt başarılı!");
    }
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .map(user -> {
                    if (!user.getPasswordHash().equals(request.getPassword())) {
                        return ResponseEntity.status(401).body("Şifre hatalı.");
                    }
                    if (!user.getIsActive()) {
                        return ResponseEntity.status(403).body("Kullanıcı pasif durumda.");
                    }
                    return ResponseEntity.ok("Giriş başarılı. Hoş geldiniz, " + user.getFullName() + "!");
                })
                .orElse(ResponseEntity.status(404).body("E-posta bulunamadı."));
    }

}
