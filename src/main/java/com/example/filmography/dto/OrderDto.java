package com.example.filmography.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto extends GenericDto{

    private LocalDateTime rentDate;
    private LocalDateTime returnDate;
    private Integer rentPeriod;
    private boolean returned;
    private boolean purchase;
    private Integer amount;
    private Long filmId;
    private Long userId;
    private FilmDto film;
    private UserDto user;

}
