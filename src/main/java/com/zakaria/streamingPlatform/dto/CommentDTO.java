package com.zakaria.streamingPlatform.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private LocalDateTime date;
    private String content;
    private Boolean approved;
    private Long movieId;
    private String movieTitle;
    private Long parentComment; //Nested comments
    private UserPublicDTO user;
    private Boolean likedByCurrentUser;

    public CommentDTO() {
    }

    public CommentDTO(Long id, LocalDateTime date, String content, Boolean approved, Long movieId, String movieTitle,
                      Long parentComment, UserPublicDTO user, Boolean likedByCurrentUser) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.approved = approved;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.parentComment = parentComment;
        this.user = user;
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public Long getParentComment() {
        return parentComment;
    }

    public void setParentComment(Long parentComment) {
        this.parentComment = parentComment;
    }

    public UserPublicDTO getUser() {
        return user;
    }

    public void setUser(UserPublicDTO user) {
        this.user = user;
    }

    public Boolean getLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(Boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }
}
