package com.simplon.api.RestEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class UserDTO {

    private String email;
    private String userName;
}
