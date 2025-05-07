package com.zakaria.streamingPlatform.user;

import com.zakaria.streamingPlatform.entities.UserEntity;
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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> userEntity = userRepository.findByEmail(email);

        if (userEntity.isEmpty()) {
            System.out.println("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(userEntity.get());
    }
}
