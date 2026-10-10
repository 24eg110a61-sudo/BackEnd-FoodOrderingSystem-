package com.foodorder.controller;

import com.foodorder.model.*;
import com.foodorder.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderRepository orders;
    private final UserRepository users;
    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;

    public record Line(@NotNull Long menuItemId, @Min(1) int quantity) {}
    public record OrderRequest(@NotNull Long userId, @NotNull Long restaurantId,
                               @NotBlank String address, @NotEmpty List<@Valid Line> items) {}

    @PostMapping
    @Transactional
    public Order place(@Valid @RequestBody OrderRequest req) {
        User user = users.findById(req.userId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Restaurant restaurant = restaurants.findById(req.restaurantId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setAddress(req.address());

        BigDecimal total = BigDecimal.ZERO;
        for (Line line : req.items()) {
            MenuItem m = menu.findById(line.menuItemId())
                    .filter(x -> x.isAvailable() && x.getRestaurant().getId().equals(restaurant.getId()))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Invalid menu item " + line.menuItemId()));
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setMenuItem(m);
            oi.setQuantity(line.quantity());
            oi.setUnitPrice(m.getPrice());
            order.getItems().add(oi);
            total = total.add(m.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotal(total);
        return orders.save(order);
    }

    @GetMapping
    public List<Order> byUser(@RequestParam Long userId) {
        return orders.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public List<Order> byRestaurant(@PathVariable Long restaurantId) {
        return orders.findByRestaurantIdOrderByCreatedAtDesc(restaurantId);
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam Order.Status status) {
        Order o = orders.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        o.setStatus(status);
        return orders.save(o);
    }
}
