package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.MovieCastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieCastRepository extends JpaRepository<MovieCastEntity, Long> {
}
