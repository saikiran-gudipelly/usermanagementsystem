package com.usermanagementsystem.controller;

import com.usermanagementsystem.dto.CreateUserRequest;
import com.usermanagementsystem.dto.UpdateUserStatusRequest;
import com.usermanagementsystem.dto.UserResponse;
import com.usermanagementsystem.enums.UserStatus;
import com.usermanagementsystem.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create user
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    // Get user by id
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    // Update user status
    @PatchMapping("/{id}/status")
    public UserResponse updateUserStatus(
            @PathVariable String id,
            @Valid @RequestBody UpdateUserStatusRequest request) {

        return userService.updateUserStatus(id, request);
    }

    // List all users
    @GetMapping
    public Page<UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String search
    ) {
        return userService.getUsers(page, size, status, search);
    }

    //Delete user by id
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
    }
}