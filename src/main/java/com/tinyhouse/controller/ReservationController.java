package com.tinyhouse.controller;

import com.tinyhouse.model.*;
import com.tinyhouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HouseRepository houseRepository;

    // ✔ Yeni rezervasyon oluştur
    @PostMapping("/add")
    public ResponseEntity<String> createReservation(@RequestBody Reservation reservation) {

        // 1. Ev ve kullanıcı var mı?
        if (reservation.getHouse() == null || reservation.getHouse().getHouseID() == null)
            return ResponseEntity.badRequest().body("Ev bilgisi eksik.");
        if (reservation.getUser() == null || reservation.getUser().getUserID() == null)
            return ResponseEntity.badRequest().body("Kullanıcı bilgisi eksik.");

        House house = houseRepository.findById(reservation.getHouse().getHouseID()).orElse(null);
        User user = userRepository.findById(reservation.getUser().getUserID()).orElse(null);
        if (house == null || user == null)
            return ResponseEntity.badRequest().body("Ev veya kullanıcı bulunamadı.");

        // 2. Tarih kontrolü: çakışan rezervasyon var mı?
        List<Reservation> conflicts = reservationRepository
                .findByHouseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                        house, reservation.getEndDate(), reservation.getStartDate());
        if (!conflicts.isEmpty()) {
            return ResponseEntity.badRequest().body("Bu tarihlerde ev zaten rezerve edilmiş.");
        }

        // 3. Gün sayısını hesapla
        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate()) + 1;
        double total = days * house.getPricePerNight();

        reservation.setHouse(house);
        reservation.setUser(user);
        reservation.setTotalPrice(total);
        reservation.setPaymentStatus("beklemede");

        reservationRepository.save(reservation);

        return ResponseEntity.ok("Rezervasyon başarılı! Toplam ücret: " + total + "₺");
    }

    // ✔ Kullanıcının rezervasyonları
    @GetMapping("/user/{userId}")
    public List<Reservation> getUserReservations(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return List.of();
        return reservationRepository.findByUser(user);
    }

    // ✔ Bir evin tüm rezervasyonları
    @GetMapping("/house/{houseId}")
    public List<Reservation> getHouseReservations(@PathVariable Long houseId) {
        House house = houseRepository.findById(houseId).orElse(null);
        if (house == null) return List.of();
        return reservationRepository.findByHouse(house);
    }
}
