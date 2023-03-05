package com.example.filmography.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class GenericDto {

    private Long id;
    private String createdBy;
    private LocalDateTime createdWhen;
    private LocalDateTime updatedWhen;
    private String updatedBy;
    private boolean isDeleted;
    private LocalDateTime deletedWhen;
    private String deletedBy;


}

