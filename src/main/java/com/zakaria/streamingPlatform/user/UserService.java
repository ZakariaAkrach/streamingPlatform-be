package com.zakaria.streamingPlatform.user;

import com.zakaria.streamingPlatform.entities.Role;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.jwt.JWTService;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.models.UserModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
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
        Response<UserModel> response = new Response<>();
        UserValidator userValidator = new UserValidator();

        List<String> errors = userValidator.validate(userModel);

        if (!errors.isEmpty()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Validation failed");
            response.setError(errors);

            return response;
        }

        Optional<UserEntity> existEmail = userRepository.findByEmail(userModel.getEmail());

        if (existEmail.isPresent()) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setMessage("Email is already in use");
            response.setError(List.of("Email is already in use"));
            return response;
        }

        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        userModel.setDateCreated(LocalDate.now());
        userModel.setActive(false);
        userModel.setRole(Role.USER);
        UserEntity userEntity = userMapper.convertToEntity(userModel);
        UserEntity savedUser = userRepository.save(userEntity);

        UserModel responseModel = userMapper.convertToModel(savedUser);
        responseModel.setPassword(null);

        response.setStatus(HttpStatus.CREATED.value());
        response.setMessage("User created successfully");
        response.setData(responseModel);
        return response;
    }

    public ResponseToken login(UserModel userModel) {
        ResponseToken responseToken = new ResponseToken();

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userModel.getEmail());
                responseToken.setStatus(HttpStatus.OK.value());
                responseToken.setMessage("Token created successfully");
                responseToken.setToken(token);
                return responseToken;
            }
        } catch (AuthenticationException e) {
            responseToken.setStatus(HttpStatus.UNAUTHORIZED.value());
            responseToken.setMessage("Invalid credentials, token generation failed");
            responseToken.setToken(null);
            return responseToken;
        }
        return responseToken;
    }
}
