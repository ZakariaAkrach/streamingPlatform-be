package com.zakaria.streamingPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.zakaria.streamingPlatform.*" })
public class StreamingPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamingPlatformApplication.class, args);
	}

}
