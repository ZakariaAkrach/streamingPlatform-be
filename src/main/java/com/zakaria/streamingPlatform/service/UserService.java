package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.dto.UserPublicDTO;
import com.zakaria.streamingPlatform.entities.Role;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.jwt.JWTService;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.repository.UserRepository;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
import com.zakaria.streamingPlatform.utils.Utils;
import com.zakaria.streamingPlatform.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public Response<String> register(UserDTO userDTO) {
        UserValidator userValidator = new UserValidator();

        List<String> errors = userValidator.validate(userDTO);

        if (!errors.isEmpty()) {
            return Utils.createResponse(HttpStatus.BAD_REQUEST.value(), "Validation failed", errors, null);
        }
        Optional<UserEntity> existEmail = userRepository.findByEmail(userDTO.getEmail());

        if (existEmail.isPresent()) {
            logger.error("Email is already in use {}", userDTO.getEmail());
            return Utils.createResponse(HttpStatus.BAD_REQUEST.value(),
                    "Email is already in use", List.of("Email is already in use"), null);
        }
        Optional<UserEntity> existUsername = userRepository.findByUsername(userDTO.getUsername());
        if (existUsername.isPresent()) {
            logger.error("Username is already in use {}", userDTO.getEmail());
            return Utils.createResponse(HttpStatus.BAD_REQUEST.value(),
                    "Username is already in use", List.of("Username is already in use"), null);
        }

        userDTO = prepareUserForRegistration(userDTO);

        UserDTO responseModel = userMapper.convertToModel(saveUser(userDTO));
        responseModel.setPassword(null);

        logger.error("User created successfully {}", userDTO.getEmail());
        return Utils.createResponse(HttpStatus.CREATED.value(), "User created successfully", null, "User created successfully");
    }

    public UserDTO prepareUserForRegistration(UserDTO userDTO) {
        userDTO.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        userDTO.setDateCreated(LocalDate.now());
        userDTO.setActive(true);
        userDTO.setRole(Role.USER);
        return userDTO;
    }

    public UserEntity saveUser(UserDTO userDTO) {
        UserEntity userEntity = userMapper.convertToEntity(userDTO);
        return userRepository.save(userEntity);
    }

    public ResponseToken login(UserDTO userDTO) {
        String errorMessageValidation = loginValidation(userDTO.getEmail());

        if (!errorMessageValidation.isEmpty()) {
            return Utils.createResponseToken(HttpStatus.UNAUTHORIZED.value(), errorMessageValidation, null);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userDTO.getEmail());
                logger.error("Token created successfully {}", token);
                return Utils.createResponseToken(HttpStatus.OK.value(), "Token created successfully", token);
            }
        } catch (AuthenticationException e) {
            logger.error("Invalid credentials for generation token");
            return Utils.createResponseToken(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials.", null);
        }
        return Utils.createResponseToken(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unexpected error during login.", null);
    }

    public String loginValidation(String email) {
        Optional<UserEntity> findByEmail = userRepository.findByEmail(email);

        if (findByEmail.isEmpty()) {
            logger.error("Email not found {}", email);
            return "Email not found.";
        }
        if (!findByEmail.get().isActive()) {
            logger.error("Account deactivated. Please contact administration {}", email);
            return "Account deactivated. Please contact administration.";
        }
        return "";
    }

    public Response<UserPublicDTO> getUserInfo() {
        Optional<UserEntity> existingUser = userRepository.findById(Utils.getCurrentUserEntity().getId());
        if (existingUser.isPresent()) {
            UserPublicDTO userPublicDTO = new UserPublicDTO();
            userPublicDTO.setUsername(existingUser.get().getUsername());
            logger.info("Returned name user {}", userPublicDTO.getUsername());
            return Utils.createResponse(HttpStatus.OK.value(), "Returned current logged username", null, userPublicDTO);
        }
        logger.info("Not logged");
        return Utils.createResponse(HttpStatus.OK.value(), "Not logged", null, null);
    }
}
