package com.simplon.api.RestEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertDTO {

    private String id;
    private String cause;
    private String description;
    private String date;
    private String time;
    private String location;
    private String picture;
    private String name;
    private String firstname;
    private String userAddress;
    private String userZipcode;
    private String city;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updateBy;

}
