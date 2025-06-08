package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.GenresEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenresRepository extends JpaRepository<GenresEntity, Long> {
    Optional<GenresEntity> findByName(String name);
}
