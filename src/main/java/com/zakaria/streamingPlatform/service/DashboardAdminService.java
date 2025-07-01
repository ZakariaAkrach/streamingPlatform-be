package com.zakaria.streamingPlatform.service;

import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.mapper.UserMapper;
import com.zakaria.streamingPlatform.repository.UserRepository;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardAdminService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public DashboardAdminService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

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
}
