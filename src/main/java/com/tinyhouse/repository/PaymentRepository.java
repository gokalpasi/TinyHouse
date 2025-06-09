package com.tinyhouse.repository;

import com.tinyhouse.model.Payment;
import com.tinyhouse.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByReservation(Reservation reservation);
}
