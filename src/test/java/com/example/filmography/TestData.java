package com.example.filmography;

import java.util.List;

import com.example.filmography.dto.DirectorDto;

public class TestData {

    private static final DirectorDto DIRECTOR_DTO  = DirectorDto
            .builder()
            .directorFIO("TEST_DIRECTOR_FIO")
            .position("TEST_POSITION")
            .build();

    private static final List<DirectorDto> DIRECTOR_DTO_LIST = List.of(DIRECTOR_DTO, DIRECTOR_DTO);
}
