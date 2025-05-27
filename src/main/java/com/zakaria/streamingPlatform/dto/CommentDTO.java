package com.zakaria.streamingPlatform.dto;

import com.zakaria.streamingPlatform.entities.CommentEntity;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private LocalDateTime date;
    private String content;
    private Integer likes;
    private Integer dislike;
    private Boolean approved;
    private MovieDTO movie;
    private CommentDTO parentComment; //Nested comments

    public CommentDTO() {
    }

    public CommentDTO(Long id, LocalDateTime date, String content, Integer likes, Integer dislike,
                      Boolean approved, MovieDTO movie, CommentDTO parentComment) {
        this.id = id;
        this.date = date;
        this.content = content;
        this.likes = likes;
        this.dislike = dislike;
        this.approved = approved;
        this.movie = movie;
        this.parentComment = parentComment;
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getDislike() {
        return dislike;
    }

    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    public CommentDTO getParentComment() {
        return parentComment;
    }

    public void setParentComment(CommentDTO parentComment) {
        this.parentComment = parentComment;
    }
}
