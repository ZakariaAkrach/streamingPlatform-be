package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.dto.UserCommentLikeDTO;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/get-all-by-content/{id}")
    public Response<List<CommentDTO>> getAllByContent(@PathVariable Long id) {
        return this.commentService.getAllByContent(id);
    }

    @PostMapping("/add")
    public Response<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PostMapping("/reply")
    public Response<CommentDTO> replyComment(@RequestBody CommentDTO commentDTO) {
        return commentService.replyComment(commentDTO);
    }

    @PutMapping("modify")
    public Response<CommentDTO> modifyComment(@RequestBody CommentDTO commentDTO) {
        return commentService.modifyComment(commentDTO);
    }

    @PutMapping("like")
    public Response<UserCommentLikeDTO> likeComment(@RequestBody UserCommentLikeDTO userCommentLikeDTO) {
        return commentService.likeComment(userCommentLikeDTO);
    }
}
