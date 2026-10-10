package com.foodorder.controller;

import com.foodorder.model.MenuItem;
import com.foodorder.model.Restaurant;
import com.foodorder.repository.MenuItemRepository;
import com.foodorder.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;

    @GetMapping
    public List<Restaurant> list(@RequestParam(required = false) String q) {
        return (q == null || q.isBlank())
                ? restaurants.findByActiveTrue()
                : restaurants.findByActiveTrueAndNameContainingIgnoreCase(q);
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable Long id) {
        return restaurants.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
    }

    @GetMapping("/{id}/menu")
    public List<MenuItem> menu(@PathVariable Long id) {
        return menu.findByRestaurantIdAndAvailableTrue(id);
    }
}
