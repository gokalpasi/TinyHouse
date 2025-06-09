package com.tinyhouse.controller;

import com.tinyhouse.model.Payment;
import com.tinyhouse.model.Reservation;
import com.tinyhouse.repository.PaymentRepository;
import com.tinyhouse.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    // ✔ Ödeme Yap
    @PostMapping("/pay")
    public ResponseEntity<String> makePayment(@RequestBody Payment payment) {
        if (payment.getReservation() == null || payment.getReservation().getReservationID() == null) {
            return ResponseEntity.badRequest().body("Rezervasyon bilgisi eksik.");
        }

        Reservation reservation = reservationRepository
                .findById(payment.getReservation().getReservationID())
                .orElse(null);

        if (reservation == null) {
            return ResponseEntity.badRequest().body("Rezervasyon bulunamadı.");
        }

        if (reservation.getPaymentStatus().equalsIgnoreCase("ödendi")) {
            return ResponseEntity.badRequest().body("Bu rezervasyon zaten ödenmiş.");
        }

        // Ödeme işlemi
        payment.setReservation(reservation);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus("başarılı");
        payment.setAmount(reservation.getTotalPrice());

        paymentRepository.save(payment);

        // Rezervasyon durumunu güncelle
        reservation.setPaymentStatus("ödendi");
        reservationRepository.save(reservation);

        return ResponseEntity.ok("Ödeme başarılı. Tutar: " + payment.getAmount() + "₺");
    }

    // ✔ Tüm ödemeleri listele (isteğe bağlı)
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
}
