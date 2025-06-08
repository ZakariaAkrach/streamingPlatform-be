package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.compositeKeys.UserMovieKey;
import com.zakaria.streamingPlatform.entities.UserMovieFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieFavoriteRepository extends JpaRepository<UserMovieFavoriteEntity, UserMovieKey> {
}
