package com.zakaria.streamingPlatform.entities;

import com.zakaria.streamingPlatform.compositeKeys.UserCommentKey;
import jakarta.persistence.*;

@Entity
@Table(name = "user_comment_like")
public class UserCommentLikeEntity {
    @EmbeddedId
    private UserCommentKey userCommentKey;
    private Boolean liked;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @MapsId("commentId")
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public UserCommentLikeEntity() {
    }

    public UserCommentLikeEntity(UserCommentKey userCommentKey, Boolean liked, UserEntity user, CommentEntity comment) {
        this.userCommentKey = userCommentKey;
        this.liked = liked;
        this.user = user;
        this.comment = comment;
    }

    public UserCommentKey getUserCommentKey() {
        return userCommentKey;
    }

    public void setUserCommentKey(UserCommentKey userCommentKey) {
        this.userCommentKey = userCommentKey;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CommentEntity getComment() {
        return comment;
    }

    public void setComment(CommentEntity comment) {
        this.comment = comment;
    }
}
