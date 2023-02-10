package com.example.filmography.rest.controller;

import com.example.filmography.dto.AddFilmDto;
import com.example.filmography.dto.DirectorDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.DirectorWithFilmsMapper;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/rest/director")
public class DirectorController extends GenericController<Director, DirectorDto>{
    private final DirectorService service;
    private final DirectorWithFilmsMapper directorWithFilmsMapper;

    public DirectorController(DirectorService service, DirectorMapper mapper, DirectorWithFilmsMapper directorWithFilmsMapper) {
        super(service, mapper);
        this.service = service;
        this.directorWithFilmsMapper = directorWithFilmsMapper;
    }


    @PostMapping("/add-film")
    public DirectorDto addFilm(@RequestBody AddFilmDto addFilmDto) {
       return directorWithFilmsMapper.toDto(service.addFilm(addFilmDto));
    }

}
