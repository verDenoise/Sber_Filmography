package com.example.filmography.dto;

import lombok.Data;

@Data
public class PurchaseFilmDto {
    Long filmId;
    Long userId;
    //Integer amount;
    Integer rentPeriod;
}
