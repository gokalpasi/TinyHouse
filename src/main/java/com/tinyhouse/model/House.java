package com.tinyhouse.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Houses")
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long houseID;

    @ManyToOne
    @JoinColumn(name = "ownerID", nullable = false)
    private User owner;

    private String title;

    @Column(length = 1000)
    private String description;

    private Double pricePerNight;

    private String location;

    @Column(nullable = false)
    private String status; // aktif, pasif, silinmiş

    // ---- Getter ve Setter'lar ----

    public Long getHouseID() {
        return houseID;
    }

    public void setHouseID(Long houseID) {
        this.houseID = houseID;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
