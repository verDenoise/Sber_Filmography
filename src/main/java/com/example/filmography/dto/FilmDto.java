package com.example.filmography.dto;

import com.example.filmography.model.Genre;
import lombok.*;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilmDto extends GenericDto {

    private String title;
    private String premierYear;
    private String country;
    private Genre genre;
    private Set<Long> directorsIds;
}
