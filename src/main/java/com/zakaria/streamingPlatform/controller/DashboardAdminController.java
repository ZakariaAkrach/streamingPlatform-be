package com.zakaria.streamingPlatform.controller;

import com.zakaria.streamingPlatform.dto.MovieWithFavoriteCountDTO;
import com.zakaria.streamingPlatform.dto.UserDTO;
import com.zakaria.streamingPlatform.entities.TypeMovie;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.service.DashboardAdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin-dashboard")
public class DashboardAdminController {

    private final DashboardAdminService dashboardAdminService;

    public DashboardAdminController(DashboardAdminService dashboardAdminService) {
        this.dashboardAdminService = dashboardAdminService;
    }

    @GetMapping("/get-all-users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponsePagination<UserDTO> getAllUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "") String username
    ) {
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return this.dashboardAdminService.getAllUser(pageable, username);
    }

    @GetMapping("/get-top-5-content/{typeMovie}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<List<MovieWithFavoriteCountDTO>> getTopFiveFavoriteContent(@PathVariable(name = "typeMovie") TypeMovie typeMovie,
                                                                               @RequestParam(defaultValue = "0") int topNumber) {
        return this.dashboardAdminService.getTopFiveFavoriteContent(typeMovie, topNumber);
    }

    @PutMapping("/change-users-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> changeUserStatus(@PathVariable(name = "id") Long id,
                                             @RequestParam(defaultValue = "false") boolean userStatus) {
        return this.dashboardAdminService.changeUserStatus(id, userStatus);
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public Response<String> register(@RequestBody UserDTO userDTO) {
        return dashboardAdminService.register(userDTO);
    }
}
