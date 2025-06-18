package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.compositeKeys.UserCommentKey;
import com.zakaria.streamingPlatform.entities.UserCommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLikeEntity, UserCommentKey> {

    @Modifying
    @Query("DELETE FROM UserCommentLikeEntity uml WHERE uml.comment.movie.id = :movieId")
    void deleteAllByMovieId(@Param("movieId") Long movieId);
}
