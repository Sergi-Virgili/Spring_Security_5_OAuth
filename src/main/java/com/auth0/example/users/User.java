package com.auth0.example.users;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

}
