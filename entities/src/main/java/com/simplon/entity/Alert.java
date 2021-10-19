package com.simplon.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "alert")
public class Alert {

    public Alert() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @Id
    @Column(unique = true, nullable = false)
    private String id;

    private String cause;

    private String description;

    private String date;

    private String time;

    private String location;

    private String picture;

    private String name;

    private String firstname;

    private String userAddress;

    private String userZipcode;

    private String city;

    private String phoneNumber;

    @Column(nullable = false)
    private boolean fix = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String updatedBy;


}
