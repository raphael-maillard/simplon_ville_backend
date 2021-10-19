package com.simplon.api.RestEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDTO {

    @Email
    private String email;

    private String userName;

    private String password;

    private String newPassword;
}
