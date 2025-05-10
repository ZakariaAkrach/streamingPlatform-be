package com.zakaria.streamingPlatform.utils;

import com.zakaria.streamingPlatform.models.UserModel;
import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;

import java.util.List;

public final class Utils {

    private Utils() {
    }

    public static Response<UserModel> createResponse(int httpStatus, String message, List<String> errors, UserModel responseModel) {
        Response<UserModel> response = new Response<>();

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
}
