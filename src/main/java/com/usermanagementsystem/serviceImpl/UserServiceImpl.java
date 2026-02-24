package com.usermanagementsystem.serviceImpl;

import com.usermanagementsystem.dto.CreateUserRequest;
import com.usermanagementsystem.dto.UpdateUserStatusRequest;
import com.usermanagementsystem.dto.UserResponse;
import com.usermanagementsystem.enums.UserStatus;
import com.usermanagementsystem.exception.DuplicateUserException;
import com.usermanagementsystem.exception.UserNotFoundException;
import com.usermanagementsystem.model.User;
import com.usermanagementsystem.repository.UserRepository;
import com.usermanagementsystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(CreateUserRequest request) {

        // Check duplicate email
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new DuplicateUserException("User with this email already exists");
                });

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .status(UserStatus.ACTIVE) // default status
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUserStatus(String id, UpdateUserStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setStatus(request.getStatus());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public Page<UserResponse> getUsers(int page, int size, UserStatus status, String search) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<User> userPage;

        if (search != null && !search.isBlank()) {
            userPage = userRepository
                    .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            search,
                            search,
                            pageable
                    );
        } else if (status != null) {
            userPage = userRepository.findByStatus(status, pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        return userPage.map(this::mapToResponse);
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        userRepository.delete(user);
    }
}