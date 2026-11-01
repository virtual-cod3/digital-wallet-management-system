
package com.jsp.wallet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.jsp.wallet.dto.AuthResponse;
import com.jsp.wallet.dto.UserLoginRequest;
import com.jsp.wallet.dto.UserRegistrationRequest;
import com.jsp.wallet.dto.UserResponse;
import com.jsp.wallet.entity.User;
import com.jsp.wallet.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request) {

        User registeredUser = userService.registerUser(request);

        UserResponse response = new UserResponse(
                registeredUser.getId(),
                registeredUser.getFullName(),
                registeredUser.getEmail(),
                registeredUser.getPhoneNumber()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody UserLoginRequest request) {

        AuthResponse response = userService.loginUser(request);

        return ResponseEntity.ok(response);
    }
}
