package com.zakaria.streamingPlatform.validator;

import com.zakaria.streamingPlatform.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserValidator {
    public List<String> validate(UserModel userModel) {
        List<String> errors = new ArrayList<>();

        if (userModel == null) {
            errors.add("No data of user send to register");
            return errors;
        }
        if (userModel.getEmail() == null || userModel.getEmail().isEmpty()) {
            errors.add("Missing field email");
        }
        if (userModel.getUsername() == null || userModel.getUsername().isEmpty()) {
            errors.add("Missing field username");
        }
        if (userModel.getPassword() == null || userModel.getPassword().isEmpty()) {
            errors.add("Missing field password");
        }
        return errors;
    }
}
