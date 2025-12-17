package com.prpo.chat.encryption.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prpo.chat.encryption.api.dto.PasswordHashDto;
import com.prpo.chat.encryption.services.password.PasswordHashingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/password")
public class PasswordHashController {

    private final PasswordHashingService passwordHashingService;

    public PasswordHashController(PasswordHashingService passwordHashingService) {
        this.passwordHashingService = passwordHashingService;
    }

    @Operation(
        summary = "Hash a password",
        description = "Hashes a plain text password and returns the hashed value"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password hashed successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid password input")
    })
    @PostMapping
    public PasswordHashDto hashPassword(
        @Parameter(description = "Password hashing payload", required = true)
        @RequestBody PasswordHashDto body
    ) throws Exception {
        String hashedPassword = passwordHashingService.hashPassword(body.getPassword());
        body.setHashedPassword(hashedPassword);
        return body;
    }

    @Operation(
        summary = "Validate a password",
        description = "Validates a plain text password against a hashed password"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Password validation result returned"),
        @ApiResponse(responseCode = "400", description = "Invalid password payload")
    })
    @PostMapping("/validation")
    public Boolean validatePassword(
        @Parameter(description = "Password validation payload", required = true)
        @RequestBody PasswordHashDto body
    ) throws Exception {
        return passwordHashingService.comparePassword(
            body.getPassword(),
            body.getHashedPassword()
        );
    }
}
