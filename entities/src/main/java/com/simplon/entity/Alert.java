package com.simplon.entity;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
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
    private boolean fix = false ;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String updateBy;

    @ManyToOne
    @JoinTable(
            name = "user_alert",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "alert_id")})
    private Set user;

}
