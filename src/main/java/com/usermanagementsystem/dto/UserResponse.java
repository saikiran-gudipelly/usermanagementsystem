package com.usermanagementsystem.dto;

import com.usermanagementsystem.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {

    private String id;

    private String name;

    private String email;

    private UserStatus status;

    private Instant  createdAt;

    private Instant updatedAt;
}