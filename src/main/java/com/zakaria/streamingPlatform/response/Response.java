package com.zakaria.streamingPlatform.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Response<T> {
    private int status;
    private String message;
    private T data;
    private Object error;
}
