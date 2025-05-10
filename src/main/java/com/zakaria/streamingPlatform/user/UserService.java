package com.zakaria.streamingPlatform.user;

import com.zakaria.streamingPlatform.entities.Role;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.jwt.JWTService;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.models.UserModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
import com.zakaria.streamingPlatform.utils.Utils;
import com.zakaria.streamingPlatform.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public UserService(UserRepository userRepository,
                       JWTService jwtService,
                       UserMapper userMapper,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public Response<UserModel> register(UserModel userModel) {
        UserValidator userValidator = new UserValidator();

        List<String> errors = userValidator.validate(userModel);

        if (!errors.isEmpty()) {
            return Utils.createResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors, null);
        }
        Optional<UserEntity> existEmail = userRepository.findByEmail(userModel.getEmail());

        if (existEmail.isPresent()) {
            return Utils.createResponse(HttpStatus.BAD_REQUEST.value(),
                    "Email is already in use", List.of("Email is already in use"), null);
        }
        userModel = prepareUserForRegistration(userModel);

        UserModel responseModel = userMapper.convertToModel(saveUser(userModel));
        responseModel.setPassword(null);

        return Utils.createResponse(HttpStatus.CREATED.value(), "User created successfully", null, responseModel);
    }

    public UserModel prepareUserForRegistration(UserModel userModel) {
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        userModel.setDateCreated(LocalDate.now());
        userModel.setActive(true);
        userModel.setRole(Role.USER);
        return userModel;
    }

    public UserEntity saveUser(UserModel userModel) {
        UserEntity userEntity = userMapper.convertToEntity(userModel);
        return userRepository.save(userEntity);
    }

    public ResponseToken login(UserModel userModel) {
        String errorMessageValidation = loginValidation(userModel.getEmail());

        if (!errorMessageValidation.isEmpty()) {
            return Utils.createResponseToken(HttpStatus.UNAUTHORIZED.value(), errorMessageValidation, null);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userModel.getEmail());
                return Utils.createResponseToken(HttpStatus.OK.value(), "Token created successfully", token);
            }
        } catch (AuthenticationException e) {
            return Utils.createResponseToken(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials.", null);
        }
        return Utils.createResponseToken(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error during login.", null);
    }

    public String loginValidation(String email) {
        Optional<UserEntity> findByEmail = userRepository.findByEmail(email);

        if (findByEmail.isEmpty()) {
            return "Email not found.";
        }
        if (!findByEmail.get().isActive()) {
            return "Account deactivated. Please contact administration.";
        }
        return "";
    }
}
