package com.simplon.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @Column(unique = true,nullable = false)
    private String id;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name="user_name",nullable = false)
    private String userName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    private String providerId;

    public User() {
        this.id = UUID.randomUUID().toString();
    }
}