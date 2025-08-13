package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByUsername(String username);

    Page<UserEntity> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}
