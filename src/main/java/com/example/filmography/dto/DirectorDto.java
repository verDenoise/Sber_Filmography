package com.example.filmography.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectorDto extends GenericDto{

    private String directorFIO;
    private String position;
    private Set<Long> filmsIds;

}
