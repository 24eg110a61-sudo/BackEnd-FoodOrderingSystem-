package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor
public class MenuItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    @Column(nullable = false)
    private BigDecimal price;
    private String category;
    private boolean available = true;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Restaurant restaurant;
}
