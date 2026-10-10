package com.foodorder.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String cuisine;
    private String address;
    private String imageUrl;
    private double rating;
    private boolean active = true;
}
