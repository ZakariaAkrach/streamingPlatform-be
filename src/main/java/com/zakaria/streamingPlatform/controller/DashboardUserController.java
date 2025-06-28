package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.CommentDTO;
import com.zakaria.streamingPlatform.dto.MovieDTO;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.service.DashboardUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-dashboard")
public class DashboardUserController {

    private final DashboardUserService dashboardUserService;

    public DashboardUserController(DashboardUserService dashboardUserService) {
        this.dashboardUserService = dashboardUserService;
    }

    @GetMapping("/get-all-favorite")
    @PreAuthorize("hasRole('USER')")
    public Response<List<MovieDTO>> getAllFavorite() {
        return this.dashboardUserService.getAllFavorite();
    }

    @GetMapping("/get-all-comment")
    @PreAuthorize("hasRole('USER')")
    public Response<List<CommentDTO>> getAllComment() {
        return this.dashboardUserService.getAllComment();
    }
}
