package com.simplon.api.mapper;

import com.simplon.api.restEntity.UserDTO;
import com.simplon.entity.User;

import java.util.Objects;

public class UserDTOMapper {

    public static UserDTO map (User user){

        if(Objects.isNull(user)){
            return null;
        }

        UserDTO userDTO = UserDTO.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .build();

        return userDTO;
    }
}
