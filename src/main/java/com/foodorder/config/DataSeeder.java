package com.foodorder.config;

import com.foodorder.model.*;
import com.foodorder.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final RestaurantRepository restaurants;
    private final MenuItemRepository menu;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (users.count() == 0) {
            User admin = new User();
            admin.setName("Admin");
            admin.setEmail("admin@food.com");
            admin.setPasswordHash(encoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            users.save(admin);
        }
        if (restaurants.count() > 0) return;

        Restaurant r1 = restaurant("Spice Garden", "Indian", "Banjara Hills", 4.5);
        item(r1, "Chicken Biryani", "Hyderabadi dum biryani", "249", "Main");
        item(r1, "Paneer Butter Masala", "Creamy tomato gravy", "199", "Main");
        item(r1, "Butter Naan", "Tandoor baked", "35", "Bread");
        item(r1, "Gulab Jamun", "2 pieces", "60", "Dessert");

        Restaurant r2 = restaurant("Pizza Corner", "Italian", "Madhapur", 4.2);
        item(r2, "Margherita Pizza", "Classic cheese", "229", "Pizza");
        item(r2, "Farmhouse Pizza", "Loaded veggies", "329", "Pizza");
        item(r2, "Garlic Bread", "With cheese dip", "129", "Sides");

        Restaurant r3 = restaurant("Dragon Wok", "Chinese", "Kukatpally", 4.0);
        item(r3, "Veg Hakka Noodles", "Wok tossed", "149", "Noodles");
        item(r3, "Chicken Manchurian", "Dry or gravy", "189", "Starters");
        item(r3, "Fried Rice", "Egg fried rice", "159", "Rice");
    }

    private Restaurant restaurant(String name, String cuisine, String address, double rating) {
        Restaurant r = new Restaurant();
        r.setName(name); r.setCuisine(cuisine); r.setAddress(address); r.setRating(rating);
        return restaurants.save(r);
    }

    private void item(Restaurant r, String name, String desc, String price, String category) {
        MenuItem m = new MenuItem();
        m.setRestaurant(r); m.setName(name); m.setDescription(desc);
        m.setPrice(new BigDecimal(price)); m.setCategory(category);
        menu.save(m);
    }
}
