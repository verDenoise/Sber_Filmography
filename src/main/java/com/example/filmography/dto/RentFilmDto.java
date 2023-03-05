package com.example.filmography.dto;

import lombok.Data;

@Data
public class RentFilmDto{
    Long filmId;
    Long userId;
    //Integer amount;
    Integer rentPeriod;
}
