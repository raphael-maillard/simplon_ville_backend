package com.simplon.api.Mapper;

import com.simplon.api.RestEntity.UserDTO;
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
