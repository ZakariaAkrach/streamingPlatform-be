package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public Response<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PutMapping("modify")
    public Response<CommentDTO> modifyComment(@RequestBody CommentDTO commentDTO) {
        return commentService.modifyComment(commentDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Response<CommentDTO> deleteComment(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
