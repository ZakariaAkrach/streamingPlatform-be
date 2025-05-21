package com.zakaria.streamingPlatform.validator;

import com.zakaria.streamingPlatform.dto.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    public List<String> validate(UserDTO userDTO) {
        List<String> errors = new ArrayList<>();

        if (userDTO == null) {
            errors.add("No data of user send to register");
            return errors;
        }
        if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()) {
            errors.add("Missing field email");
        }
        if (userDTO.getUsername() == null || userDTO.getUsername().isEmpty()) {
            errors.add("Missing field username");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            errors.add("Missing field password");
        }
        return errors;
    }
}
