package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.MovieWithFavoriteCountDTO;
import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.entities.Role;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.mapper.MovieMapper;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.repository.MovieRepository;
import com.zakaria.streamingPlatform.repository.UserRepository;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
import com.zakaria.streamingPlatform.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardAdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public DashboardAdminService(UserRepository userRepository, UserMapper userMapper, MovieRepository movieRepository, MovieMapper movieMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    private static final Logger logger = LoggerFactory.getLogger(DashboardAdminService.class);

    public ResponsePagination<UserDTO> getAllUser(Pageable pageable, String username) {
        Page<UserEntity> userEntityPage = this.userRepository.findByUsernameContainingIgnoreCase(username, pageable);

        if (!userEntityPage.isEmpty()) {
            logger.info("Get all users successfully");
            List<UserDTO> userDTOS = userMapper.convertToModel(userEntityPage.getContent());

            return Utils.createResponsePagination(HttpStatus.OK.value(), "Get all users successfully", userDTOS, pageable.getPageNumber(),
                    pageable.getPageSize(), userEntityPage.getTotalPages(), userEntityPage.getTotalElements(), userEntityPage.isLast(), null);
        }
        logger.info("No user available");
        return Utils.createResponsePagination(HttpStatus.NO_CONTENT.value(), "No user available", "No user available");
    }

    public Response<List<MovieWithFavoriteCountDTO>> getTopFiveFavoriteContent(TypeMovie typeMovie, int topNumber) {
        Pageable pageable = PageRequest.of(0, topNumber);
        List<Object[]> movieEntityList = this.movieRepository.topFiveFavoriteContent(typeMovie, pageable);

        if (!movieEntityList.isEmpty()) {
            logger.info("Get top content successfully");
            List<MovieWithFavoriteCountDTO> movieWithFavoriteCountDTOS = movieEntityList.stream()
                    .map(objArr -> {
                        Long id = (Long) objArr[0];
                        String title = (String) objArr[1];
                        Long favoriteCount = (Long) objArr[2];
                        return new MovieWithFavoriteCountDTO(id, title, favoriteCount);
                    })
                    .toList();

            return Utils.createResponse(HttpStatus.OK.value(), "Get top content successfully", null, movieWithFavoriteCountDTOS);
        }
        logger.info("No content found");
        return Utils.createResponse(HttpStatus.NOT_FOUND.value(), "No content found", List.of("No content found"), null);
    }

    public Response<String> changeUserStatus(Long id, boolean userStatus) {
        Optional<UserEntity> existUser = this.userRepository.findById(id);

        if(existUser.isPresent()) {
            existUser.get().setActive(userStatus);
            this.userRepository.save(existUser.get());

            logger.info("User ID {} change status correctly", id);
            return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "User change status correctly");
        } else {
            logger.info("User ID {} not found", id);
            return Utils.createResponse(HttpStatus.NO_CONTENT.value(), "No user present");
        }
    }

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
        userDTO.setRole(userDTO.getRole());
        return userDTO;
    }

    public UserEntity saveUser(UserDTO userDTO) {
        UserEntity userEntity = userMapper.convertToEntity(userDTO);
        return userRepository.save(userEntity);
    }
}
