package com.usermanagementsystem.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.usermanagementsystem.dto.UserResponse;
import com.usermanagementsystem.enums.UserStatus;
import com.usermanagementsystem.service.UserService;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {


    @Configuration
    @Import(UserController.class)
    static class TestConfig {}

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private UserResponse commonResponse;

    @BeforeEach
    void setUp() {
        commonResponse = UserResponse.builder()
                .id("test-id-123")
                .name("Sai")
                .email("sai@test.com")
                .status(UserStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("POST /api/users - Success")
    void createUser_ok() throws Exception {
        when(userService.createUser(any())).thenReturn(commonResponse);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "name": "Sai",
                          "email": "sai@test.com"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-id-123"))
                .andExpect(jsonPath("$.name").value("Sai"));
    }

    @Test
    @DisplayName("GET /api/users/{id} - Success")
    void getUserById_ok() throws Exception {
        when(userService.getUserById("test-id-123")).thenReturn(commonResponse);

        mockMvc.perform(get("/api/users/test-id-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("sai@test.com"));
    }

    @Test
    @DisplayName("PATCH /api/users/{id}/status - Success")
    void updateUserStatus_ok() throws Exception {

        commonResponse.setStatus(UserStatus.INACTIVE);

        when(userService.updateUserStatus(any(), any()))
                .thenReturn(commonResponse);

        mockMvc.perform(patch("/api/users/test-id-123/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "status":"INACTIVE"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    @DisplayName("GET /api/users - Paginated Success")
    void getUsers_ok() throws Exception {
        PageImpl<UserResponse> pageResponse = new PageImpl<>(List.of(commonResponse));

        when(userService.getUsers(anyInt(), anyInt(), any(), any()))
                .thenReturn(pageResponse);

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Sai"));
    }

    @Test
    @DisplayName("DELETE /api/users/{id} - Success")
    void deleteUser_ok() throws Exception {
        doNothing().when(userService).deleteUser("test-id-123");

        mockMvc.perform(delete("/api/users/test-id-123"))
                .andExpect(status().isOk());
    }
}