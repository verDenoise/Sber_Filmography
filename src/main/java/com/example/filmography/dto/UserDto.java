package com.example.filmography.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto extends GenericDto{

    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String address;
    private String email;
    private Date createdWhen;
    private RoleDto role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

}
