package com.ecommerce.ApiResponse;


import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "success", "statusCode", "message", "data" })
@Builder
public class ApiResponse {

    private boolean success;
    private int statusCode;
    private String message;
    private Object data;
}