package com.simplon.api.Mapper;

import com.simplon.api.RestEntity.AlertDTO;
import com.simplon.entity.Alert;

import java.util.Objects;

public class AlertDTOMapper {

    public static AlertDTO map (Alert alert){

        if(Objects.isNull(alert)){
            return null;
        }

        AlertDTO result = AlertDTO.builder()
                .cause(alert.getCause())
                .description(alert.getDescription())
                .city(alert.getCity())
                .date(alert.getDate())
                .time(alert.getTime())
                .location(alert.getLocation())
                .picture(alert.getPicture())
                .firstname(alert.getFirstname())
                .name(alert.getName())
                .phoneNumber(alert.getPhoneNumber())
                .userZipcode(alert.getUserZipcode())
                .userAddress(alert.getUserAddress())
                .createdAt(alert.getCreatedAt())
                .updateBy(alert.getUpdateBy())
                .updatedAt(alert.getUpdatedAt())
                .build();

        return result;
    }
}
