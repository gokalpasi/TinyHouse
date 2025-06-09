package com.tinyhouse.repository;

import com.tinyhouse.model.Reservation;
import com.tinyhouse.model.User;
import com.tinyhouse.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByHouse(House house);

    // Çakışan rezervasyonları kontrol etmek için:
    List<Reservation> findByHouseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            House house, LocalDate endDate, LocalDate startDate);
}
