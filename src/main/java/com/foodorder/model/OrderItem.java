package com.foodorder.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter @NoArgsConstructor
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(optional = false)
    private Order order;
    @ManyToOne(optional = false)
    private MenuItem menuItem;
    private int quantity;
    private BigDecimal unitPrice;
}
