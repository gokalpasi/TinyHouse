package com.tinyhouse.repository;

import com.tinyhouse.model.House;
import com.tinyhouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseRepository extends JpaRepository<House, Long> {
    List<House> findByOwner(User owner); // Ev sahibinin ilanlarını getir
    List<House> findByStatus(String status); // Sadece aktif ilanları getir
}
