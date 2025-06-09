package com.tinyhouse.controller;

import com.tinyhouse.model.User;
import com.tinyhouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.tinyhouse.model.House;
import com.tinyhouse.repository.HouseRepository;
import com.tinyhouse.model.Review;
import com.tinyhouse.repository.ReviewRepository;
import com.tinyhouse.model.Reservation;
import com.tinyhouse.repository.ReservationRepository;





@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReservationRepository reservationRepository;


    // ✔ Tüm kullanıcıları listele
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    // ✔ Belirli kullanıcıyı pasifleştir
    @PutMapping("/users/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setIsActive(false);
        userRepository.save(user);
        return ResponseEntity.ok("Kullanıcı pasif hale getirildi.");
    }

    // ✔ Belirli kullanıcıyı tamamen sil
    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.ok("Kullanıcı silindi.");
    }
    // ✔ Tüm ev ilanlarını listele
    @GetMapping("/houses")
    public List<House> getAllHouses() {
        return houseRepository.findAll();
    }

    // ✔ İlanı pasifleştir
    @PutMapping("/houses/{id}/deactivate")
    public ResponseEntity<String> deactivateHouse(@PathVariable Long id) {
        House house = houseRepository.findById(id).orElse(null);
        if (house == null) {
            return ResponseEntity.notFound().build();
        }

        house.setStatus("pasif");
        houseRepository.save(house);
        return ResponseEntity.ok("İlan pasifleştirildi.");
    }

    // ✔ İlanı tamamen sil
    @DeleteMapping("/houses/{id}")
    public ResponseEntity<String> deleteHouse(@PathVariable Long id) {
        if (!houseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        houseRepository.deleteById(id);
        return ResponseEntity.ok("İlan silindi.");
    }
    // ✔ Tüm yorumları listele
    @GetMapping("/reviews")
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    // ✔ Yorumu sil
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reviewRepository.deleteById(id);
        return ResponseEntity.ok("Yorum silindi.");
    }
    // ✔ Tüm rezervasyonları listele
    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    // ✔ Rezervasyonu sil (iptal gibi)
    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        if (!reservationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        reservationRepository.deleteById(id);
        return ResponseEntity.ok("Rezervasyon silindi.");
    }

}
