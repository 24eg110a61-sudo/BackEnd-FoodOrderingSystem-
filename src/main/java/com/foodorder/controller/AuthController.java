package com.foodorder.controller;

import com.foodorder.model.User;
import com.foodorder.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public record RegisterRequest(@NotBlank String name, @Email @NotBlank String email,
                                  @Size(min = 6) String password, String phone, String address) {}
    public record LoginRequest(@NotBlank String email, @NotBlank String password) {}

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest req) {
        if (users.existsByEmail(req.email()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered");
        User u = new User();
        u.setName(req.name());
        u.setEmail(req.email());
        u.setPasswordHash(encoder.encode(req.password()));
        u.setPhone(req.phone());
        u.setAddress(req.address());
        return users.save(u);
    }

    @PostMapping("/login")
    public User login(@Valid @RequestBody LoginRequest req) {
        return users.findByEmail(req.email())
                .filter(u -> encoder.matches(req.password(), u.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
    }
}
