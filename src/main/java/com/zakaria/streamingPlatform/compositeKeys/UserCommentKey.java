package com.zakaria.streamingPlatform.compositeKeys;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class UserCommentKey implements Serializable {
    private Long userId;
    private Long commentId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
