package com.usermanagementsystem.service;

import com.usermanagementsystem.dto.CreateUserRequest;
import com.usermanagementsystem.dto.UpdateUserStatusRequest;
import com.usermanagementsystem.dto.UserResponse;
import com.usermanagementsystem.enums.UserStatus;
import org.springframework.data.domain.Page;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    UserResponse getUserById(String id);

    UserResponse updateUserStatus(String id, UpdateUserStatusRequest request);

    Page<UserResponse> getUsers(int page, int size, UserStatus status, String search);

    void deleteUser(String id);
}