package com.zakaria.streamingPlatform.utils;

import com.zakaria.streamingPlatform.compositeKeys.UserCommentKey;
import com.zakaria.streamingPlatform.entities.UserEntity;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponsePagination;
import com.zakaria.streamingPlatform.response.ResponseToken;
import com.zakaria.streamingPlatform.security.user.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public final class Utils {

    private Utils() {
    }

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static <T> Response<T> createResponse(int httpStatus, String message, List<String> errors, T responseModel) {
        Response<T> response = new Response<>();

        response.setStatus(httpStatus);
        response.setMessage(message);
        response.setData(responseModel);
        response.setError(errors);
        return response;
    }

    public static Response<String> createResponse(int httpStatus, String message) {
        Response<String> response = new Response<>();

        response.setStatus(httpStatus);
        response.setMessage(message);
        return response;
    }

    public static ResponseToken createResponseToken(int httpStatus, String message, String token) {
        ResponseToken responseToken = new ResponseToken();

        responseToken.setStatus(httpStatus);
        responseToken.setMessage(message);
        responseToken.setToken(token);
        return responseToken;
    }

    public static <T> ResponsePagination<T> createResponsePagination(int status, String message, List<T> data, int page, int size, int totalPages, long totalElements, boolean isLastPage, Object error) {
        ResponsePagination<T> response = new ResponsePagination<T>();

        response.setStatus(status);
        response.setMessage(message);
        response.setData(data);
        response.setPage(page + 1); //add one human readable
        response.setSize(size);
        response.setTotalPages(totalPages);
        response.setTotalElements(totalElements);
        response.setLastPage(isLastPage);
        response.setError(error);
        return response;
    }

    public static <T> ResponsePagination<T> createResponsePagination(int status, String message, Object error) {
        ResponsePagination<T> response = new ResponsePagination<T>();

        response.setStatus(status);
        response.setMessage(message);
        response.setError(error);
        return response;
    }

    public static UserEntity getCurrentUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            logger.info("User not logged");
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        logger.info("User not logged");
        return null;
    }

    public static UserCommentKey buildUserCommentKey(Long commentEntity) {
        if(Utils.getCurrentUserEntity() == null){
            return null;
        }
        UserCommentKey userCommentKey = new UserCommentKey();
        userCommentKey.setCommentId(commentEntity);
        userCommentKey.setUserId(Utils.getCurrentUserEntity().getId());

        return userCommentKey;
    }
}
