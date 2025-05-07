package com.zakaria.streamingPlatform.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class CustomOAuth2FailureService implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Google authentication failed. Please try again.";
        String encodedError = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String redirectUrl = "http://localhost:5173/login?errorOauth2="+encodedError;
        response.sendRedirect(redirectUrl);
    }
}
