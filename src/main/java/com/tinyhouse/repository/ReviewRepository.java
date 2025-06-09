package com.tinyhouse.repository;

import com.tinyhouse.model.House;
import com.tinyhouse.model.Review;
import com.tinyhouse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHouse(House house);
    List<Review> findByUser(User user);
}
