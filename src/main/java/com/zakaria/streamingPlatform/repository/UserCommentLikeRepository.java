package com.zakaria.streamingPlatform.repository;

import com.zakaria.streamingPlatform.compositeKeys.UserCommentKey;
import com.zakaria.streamingPlatform.entities.UserCommentLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCommentLikeRepository extends JpaRepository<UserCommentLikeEntity, UserCommentKey> {
}
