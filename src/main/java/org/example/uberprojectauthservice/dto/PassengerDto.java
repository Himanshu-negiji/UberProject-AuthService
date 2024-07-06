package org.example.uberprojectauthservice.dto;

import lombok.*;
import org.example.uberprojectentityservice.models.Passenger;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDto {
    private String id;

    private String email;

    private String password;

    private String phoneNumber;

    private String name;

    private Date createdAt;

    public static PassengerDto from(Passenger p) {
        return PassengerDto.builder()
                .id(p.getName().toString())
                .createdAt(p.getCreatedAt())
                .email(p.getEmail())
                .phoneNumber(p.getPhoneNumber())
                .name(p.getName())
                .build();
    }
}