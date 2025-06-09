package com.tinyhouse.controller;

import com.tinyhouse.model.*;
import com.tinyhouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    // ✔ Yorum ekle
    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody Review review) {
        if (review.getUser() == null || review.getUser().getUserID() == null ||
                review.getHouse() == null || review.getHouse().getHouseID() == null) {
            return ResponseEntity.badRequest().body("Kullanıcı veya ev bilgisi eksik.");
        }

        if (review.getRating() < 1 || review.getRating() > 5) {
            return ResponseEntity.badRequest().body("Puan 1 ile 5 arasında olmalı.");
        }

        House house = houseRepository.findById(review.getHouse().getHouseID()).orElse(null);
        User user = userRepository.findById(review.getUser().getUserID()).orElse(null);
        if (house == null || user == null) {
            return ResponseEntity.badRequest().body("Kullanıcı veya ev bulunamadı.");
        }

        review.setCreatedAt(LocalDateTime.now());
        review.setHouse(house);
        review.setUser(user);

        reviewRepository.save(review);

        return ResponseEntity.ok("Yorum başarıyla eklendi.");
    }

    // ✔ Belirli evin yorumları
    @GetMapping("/house/{houseId}")
    public List<Review> getReviewsByHouse(@PathVariable Long houseId) {
        House house = houseRepository.findById(houseId).orElse(null);
        if (house == null) return List.of();
        return reviewRepository.findByHouse(house);
    }

    // ✔ Belirli kullanıcının yorumları
    @GetMapping("/user/{userId}")
    public List<Review> getReviewsByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return reviewRepository.findByUser(user);
    }
    // ✔ Belirli evin ortalama puanı
    @GetMapping("/house/{houseId}/average-rating")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long houseId) {
        House house = houseRepository.findById(houseId).orElse(null);
        if (house == null) {
            return ResponseEntity.notFound().build();
        }

        List<Review> reviews = reviewRepository.findByHouse(house);
        if (reviews.isEmpty()) {
            return ResponseEntity.ok(0.0);
        }

        double average = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        return ResponseEntity.ok(average);
    }

}
