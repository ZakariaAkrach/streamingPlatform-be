package com.zakaria.streamingPlatform.user;

import com.zakaria.streamingPlatform.models.UserModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Response<UserModel> register(@RequestBody UserModel userModel) {
        return userService.register(userModel);
    }

    @PostMapping("/login")
    public ResponseToken login(@RequestBody UserModel userModel) {
        return userService.login(userModel);
    }
}
