package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UserControllerIntegrationTest {

    @Autowired
    private UserController userController;

    private UserDTO userDTO;

    @BeforeEach
    void setUpBeforeEveryMethod() {
        userDTO = new UserDTO();
    }

    @Test
    @DisplayName("Should register user successfully when valid UserDTO is provided")
    void shouldRegisterUserSuccessfully_whenValidUserDTO() {
        userDTO.setUsername("Test");
        userDTO.setEmail("test@gmail.com");
        userDTO.setPassword("123");
        Response<String> response = userController.register(userDTO);

        Assertions.assertEquals("User created successfully", response.getMessage());
    }

    @Test
    @DisplayName("Should return validation error when UserDTO is null")
    void shouldReturnValidationError_whenUserDTONull() {
        Response<String> response = userController.register(null);
        Assertions.assertEquals(response.getError(), List.of("No data of user send to register"));
    }

    @Test
    @DisplayName("Should return validation error when Username is null")
    void shouldReturnValidationError_whenUserNameNull() {
        userDTO.setEmail("test@gmail.com");
        userDTO.setPassword("123");
        Response<String> response = userController.register(userDTO);
        Assertions.assertEquals(response.getError(), List.of("Missing field username"));
    }

    @Test
    @DisplayName("Should return validation error when Email is null")
    void shouldReturnValidationError_whenEmailNull() {
        userDTO.setUsername("Test");
        userDTO.setPassword("123");
        Response<String> response = userController.register(userDTO);
        Assertions.assertEquals(response.getError(), List.of("Missing field email"));
    }

    @Test
    @DisplayName("Should return validation error when Password is null")
    void shouldReturnValidationError_whenPasswordNull() {
        userDTO.setUsername("Test");
        userDTO.setEmail("test@gmail.com");
        Response<String> response = userController.register(userDTO);
        Assertions.assertEquals(response.getError(), List.of("Missing field password"));
    }

    @Test
    @DisplayName("Should return validation error when Email already exist")
    void shouldReturnValidationError_whenEmailAlreadyExist() {
        userDTO.setUsername("Test");
        userDTO.setEmail("zakaria@gmail.com");
        userDTO.setPassword("123");
        Response<String> response = userController.register(userDTO);
        Assertions.assertEquals(response.getError(), List.of("Email is already in use"));
    }
}
