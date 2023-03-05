package com.example.filmography.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto extends GenericDto{

    @NotBlank(message = "Поле не должно быть пустым")
    private String login;
    @NotBlank(message = "Поле не должно быть пустым")
    private String password;
    @NotBlank(message = "Поле не должно быть пустым")
    private String firstName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String lastName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String middleName;
    @NotBlank(message = "Поле не должно быть пустым")
    private String phone;
    @NotBlank(message = "Поле не должно быть пустым")
    private String address;
    @NotBlank(message = "Поле не должно быть пустым")
    private String email;

    private RoleDto role;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

}
