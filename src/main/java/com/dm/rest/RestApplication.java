package com.dm.rest;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "Task Manager API", version = "1.0"),
		tags ={@Tag(name = "Registration and authentication", description = "Endpoints for registration and authentication"),
			   @Tag(name = "Task operations", description = "Endpoints for tasks"),
			   @Tag(name = "Account operations", description = "Endpoints for accounts")
		}
)
@SecurityScheme(name = "BearerJWT", type = SecuritySchemeType.HTTP, scheme = "bearer",bearerFormat = "JWT",
description = "Bearer token")
public class RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestApplication.class, args);
	}
}
