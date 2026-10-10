package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {
    public enum Role { CUSTOMER, ADMIN }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String passwordHash;
    private String phone;
    private String address;
    @Enumerated(EnumType.STRING)
    private Role role = Role.CUSTOMER;
}
