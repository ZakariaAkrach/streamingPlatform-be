package com.zakaria.streamingPlatform.oauth;

import com.zakaria.streamingPlatform.entities.Role;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.jwt.JWTService;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.models.UserModel;
import com.zakaria.streamingPlatform.user.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class CustomOAuth2SuccessService implements AuthenticationSuccessHandler {
    private final JWTService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public CustomOAuth2SuccessService(JWTService jwtService, UserRepository userRepository, UserMapper userMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        UserModel userModel = new UserModel();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        Optional<UserEntity> existingEmail = userRepository.findByEmail(email);

        if (existingEmail.isEmpty()) {
            userModel.setEmail(email);
            userModel.setUsername(name);
            userModel.setRole(Role.USER);
            userModel.setPassword(generatePassword());
            userModel.setDateCreated(LocalDate.now());

            UserEntity userEntity = userMapper.convertToEntity(userModel);
            userRepository.save(userEntity);
        }
        String token = jwtService.generateToken(email);

        String redirectUrl = "http://localhost:5173/oauth2-redirect-handler?token=" + token;
        response.sendRedirect(redirectUrl);
    }

    public String generatePassword() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 15; i++) {
            int index = secureRandom.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }
}
