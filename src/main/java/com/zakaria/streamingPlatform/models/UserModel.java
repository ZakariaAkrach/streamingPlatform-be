package com.zakaria.streamingPlatform.models;

import com.zakaria.streamingPlatform.entities.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserModel {
    private String username;
    private String email;
    private String password;
    private Role role;
    private boolean active;
    private LocalDate dateCreated;
}

