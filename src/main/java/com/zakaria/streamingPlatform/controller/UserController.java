package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.dto.UserPublicDTO;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
import com.zakaria.streamingPlatform.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @PostMapping("/login")
    public ResponseToken login(@RequestBody UserDTO userDTO) {
        return userService.login(userDTO);
    }

    @GetMapping("user-info")
    public Response<UserPublicDTO> getUserInfo() {
        return userService.getUserInfo();
    }
}
