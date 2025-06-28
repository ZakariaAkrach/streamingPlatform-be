package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.compositeKeys.UserMovieKey;
import com.zakaria.streamingPlatform.entities.MovieEntity;
import com.zakaria.streamingPlatform.entities.UserMovieFavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMovieFavoriteRepository extends JpaRepository<UserMovieFavoriteEntity, UserMovieKey> {

    @Modifying
    @Query("DELETE FROM UserMovieFavoriteEntity uml WHERE uml.movie.id = :movieId")
    void deleteAllByMovieId(@Param("movieId") Long movieId);

    @Query("SELECT m.movie " +
            " FROM UserMovieFavoriteEntity m " +
            " WHERE m.favorite = true AND m.userMovieKey.userId = :userId")
    List<MovieEntity> findFavoriteMovieByUserId(@Param("userId") Long userId);
}
