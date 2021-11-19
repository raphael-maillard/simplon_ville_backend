package com.simplon.api.RestEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AlertDTO {

    private String id;
    @NonNull
    @NotBlank
    private String cause;
    @NonNull
    @NotBlank
    private String description;
    private String date;
    private String time;
    @NonNull
    @NotBlank
    private String location;
    private String picture;
    @NonNull
    @NotBlank
    private String name;
    @NonNull
    @NotBlank
    private String firstname;
    private String userAddress;
    private String userZipcode;
    private String city;
    private String phoneNumber;
    private boolean fix;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String updateBy;

}
