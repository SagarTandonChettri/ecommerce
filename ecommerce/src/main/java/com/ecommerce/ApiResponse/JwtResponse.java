package com.ecommerce.ApiResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String userName;
    private String role;
    private Date issuedAt;
    private Date expiry;
}