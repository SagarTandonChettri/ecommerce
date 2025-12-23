package com.ecommerce.Controller;

import com.ecommerce.ApiResponse.ApiResponse;
import com.ecommerce.ApiResponse.JwtResponse;
import com.ecommerce.Dto.LoginRequestDto;
import com.ecommerce.Entity.User;
import com.ecommerce.Service.AuthService;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody User user) {

        log.info("Register request received for email: {}", user.getEmail());

        String msg = authService.register(user);

        // ❌ Email already exists → 400 BAD REQUEST
        if (msg.toLowerCase().contains("username already existed")|| msg.toLowerCase().contains("email already existed") ) {
            log.warn("Registration failed. Reason: {}", msg);

            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .success(false)
                            .statusCode(400)
                            .message(msg)
                            .data(null)
                            .build());
        }

        // ✅ Success → 201 CREATED
        log.info("Registration successful for email: {}", user.getEmail());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.builder()
                        .success(true)
                        .statusCode(201)
                        .message(msg)
                        .data(null)
                        .build());
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequestDto) {

        log.info("Login request received for username: {}", loginRequestDto.getUserName());

        try{
            JwtResponse jwtResponse = authService.login(loginRequestDto);
            log.info("Login successful for username: {}", loginRequestDto.getUserName());

            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .success(true)
                            .statusCode(200)
                            .message("Login successful")
                            .data(jwtResponse)
                            .build()
            );
        }catch (RuntimeException ex){
            log.warn("Login failed for username: {}", loginRequestDto.getUserName());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ApiResponse.builder()
                            .success(false)
                            .statusCode(401)
                            .message(ex.getMessage())
                            .data(null)
                            .build()
            );
        }
    }
}
