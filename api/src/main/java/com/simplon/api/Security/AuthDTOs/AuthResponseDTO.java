package com.simplon.api.Security.AuthDTOs;

import lombok.Data;

import java.net.URI;

@Data
public class AuthResponseDTO {

    private URI location;
    private ApiResponse apiResponse;

}
