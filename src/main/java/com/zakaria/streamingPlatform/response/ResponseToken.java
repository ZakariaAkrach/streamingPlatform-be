package com.zakaria.streamingPlatform.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseToken {
    private int status;
    private String message;
    private String token;
}
