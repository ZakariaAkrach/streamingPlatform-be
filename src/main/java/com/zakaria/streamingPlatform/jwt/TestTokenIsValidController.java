package com.zakaria.streamingPlatform.jwt;

import com.zakaria.streamingPlatform.response.Response;
import com.zakaria.streamingPlatform.response.ResponseToken;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TestTokenIsValidController {

    private final JWTService jwtService;

    public TestTokenIsValidController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/test")
    public Response<String> test(){
        Response<String> response = new Response<>();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("token is still valid");
        return response;
    }

    @GetMapping("/refresh-token")
    public ResponseToken refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        return jwtService.refreshToken(authorizationHeader);
    }
}
