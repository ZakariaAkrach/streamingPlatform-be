package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.compositeKeys.UserMovieKey;
import com.zakaria.streamingPlatform.entities.UserMovieLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieLikeRepository extends JpaRepository<UserMovieLikeEntity, UserMovieKey> {

    @Modifying
    @Query("DELETE FROM UserMovieLikeEntity uml WHERE uml.movie.id = :movieId")
    void deleteAllByMovieId(@Param("movieId") Long movieId);
}
