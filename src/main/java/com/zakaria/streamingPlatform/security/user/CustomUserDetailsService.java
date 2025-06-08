package com.zakaria.streamingPlatform.security.user;

import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (userEntity.isEmpty()) {
            logger.info("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userEntity.get());
    }
}
