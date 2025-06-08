package com.zakaria.streamingPlatform.dto;

public class UserCommentLikeDTO {
    private long commentId;
    private Boolean liked;
    private UserPublicDTO user;

    public UserCommentLikeDTO() {
    }

    public UserCommentLikeDTO(Boolean liked, UserPublicDTO user, long commentId) {
        this.liked = liked;
        this.user = user;
        this.commentId = commentId;
    }

    public Boolean isLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public UserPublicDTO getUser() {
        return user;
    }

    public void setUser(UserPublicDTO user) {
        this.user = user;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }
}
