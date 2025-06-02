package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {

    @Query("SELECT c FROM CommentEntity c WHERE c.movie.id = :id")
    List<CommentEntity> getAllCommentByMovieId(@Param("id") Long id);
}
