package com.usermanagementsystem.serviceImpl;

import com.usermanagementsystem.dto.CreateUserRequest;
import com.usermanagementsystem.dto.UpdateUserStatusRequest;
import com.usermanagementsystem.enums.UserStatus;
import com.usermanagementsystem.exception.DuplicateUserException;
import com.usermanagementsystem.exception.UserNotFoundException;
import com.usermanagementsystem.model.User;
import com.usermanagementsystem.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldCreateUserSuccessfully() {

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Sai");
        request.setEmail("sai@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        User savedUser = User.builder()
                .id("1")
                .name("Sai")
                .email("sai@gmail.com")
                .status(UserStatus.ACTIVE)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        var response = userService.createUser(request);

        assertEquals("Sai", response.getName());
        assertEquals(UserStatus.ACTIVE, response.getStatus());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowDuplicateUserException() {

        CreateUserRequest request = new CreateUserRequest();
        request.setName("Sai");
        request.setEmail("sai@gmail.com");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(new User()));

        assertThrows(DuplicateUserException.class,
                () -> userService.createUser(request));
    }

    @Test
    void shouldGetUserByIdSuccessfully() {

        User user = User.builder()
                .id("1")
                .name("Sai")
                .email("sai@gmail.com")
                .status(UserStatus.ACTIVE)
                .build();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        var response = userService.getUserById("1");

        assertEquals("Sai", response.getName());
    }

    @Test
    void shouldThrowUserNotFoundWhenGetById() {

        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userService.getUserById("1"));
    }

    @Test
    void shouldUpdateUserStatusSuccessfully() {

        User user = User.builder()
                .id("1")
                .name("Sai")
                .email("sai@gmail.com")
                .status(UserStatus.ACTIVE)
                .build();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UpdateUserStatusRequest request = new UpdateUserStatusRequest();
        request.setStatus(UserStatus.BLOCKED);

        var response = userService.updateUserStatus("1", request);

        assertEquals(UserStatus.BLOCKED, response.getStatus());
    }

    @Test
    void shouldDeleteUserSuccessfully() {

        User user = User.builder().id("1").build();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        userService.deleteUser("1");

        verify(userRepository).delete(user);
    }

    @Test
    void shouldReturnUsersWithPagination() {

        Pageable pageable = PageRequest.of(0,10);

        Page<User> mockPage = new PageImpl<>(List.of(
                User.builder().id("1").name("Sai").email("sai@gmail.com").status(UserStatus.ACTIVE).build()
        ));

        when(userRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        var result = userService.getUsers(0,10,null,null);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldSearchUsersWhenSearchProvided() {

        Pageable pageable = PageRequest.of(0,10);

        Page<User> mockPage = new PageImpl<>(List.of(
                User.builder()
                        .id("1")
                        .name("Saikiran")
                        .email("saikiran@gmail.com")
                        .status(UserStatus.ACTIVE)
                        .build()
        ));

        when(userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
                        anyString(),
                        anyString(),
                        any(Pageable.class)))
                .thenReturn(mockPage);

        var result = userService.getUsers(0,10,null,"sai");

        assertFalse(result.isEmpty());
        assertEquals("Saikiran", result.getContent().get(0).getName());
    }
}
