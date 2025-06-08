package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.CastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CastRepository extends JpaRepository<CastEntity, Long> {
    Optional<CastEntity> findByName(String name);
}
