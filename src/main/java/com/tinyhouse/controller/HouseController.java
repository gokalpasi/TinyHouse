package com.tinyhouse.controller;

import com.tinyhouse.model.House;
import com.tinyhouse.model.User;
import com.tinyhouse.repository.HouseRepository;
import com.tinyhouse.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/houses")
public class HouseController {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private UserRepository userRepository;

    // ✔ Yeni ilan ekle (ev sahibi)
    @PostMapping("/add")
    public String addHouse(@RequestBody House house) {
        // House nesnesinde owner set edilmiş olmalı (id üzerinden)
        if (house.getOwner() == null || house.getOwner().getUserID() == null) {
            return "Ev sahibi bilgisi eksik.";
        }

        // Kullanıcı var mı kontrol et
        User owner = userRepository.findById(house.getOwner().getUserID()).orElse(null);
        if (owner == null) {
            return "Geçersiz kullanıcı (ev sahibi) ID.";
        }

        house.setOwner(owner);
        house.setStatus("aktif"); // varsayılan olarak aktif
        houseRepository.save(house);
        return "Ev başarıyla eklendi.";
    }

    // ✔ Tüm aktif ilanları getir (kiracı için)
    @GetMapping("/active")
    public List<House> getActiveHouses() {
        return houseRepository.findByStatus("aktif");
    }

    // ✔ Belirli ev sahibinin evleri
    @GetMapping("/by-owner/{ownerId}")
    public List<House> getHousesByOwner(@PathVariable Long ownerId) {
        User owner = userRepository.findById(ownerId).orElse(null);
        if (owner == null) return List.of(); // boş liste
        return houseRepository.findByOwner(owner);
    }
}
