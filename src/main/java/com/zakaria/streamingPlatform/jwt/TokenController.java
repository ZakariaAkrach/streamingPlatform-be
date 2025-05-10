package com.zakaria.streamingPlatform.jwt;

import com.zakaria.streamingPlatform.response.ResponseToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class TokenController {

    private final JWTService jwtService;

    public TokenController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/refresh-token")
    public ResponseToken refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        return jwtService.refreshToken(authorizationHeader);
    }
}
