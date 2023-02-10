package com.example.filmography.rest.controller;

import com.example.filmography.dto.FilmDto;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/film")
public class FilmController extends GenericController<Film, FilmDto>{


    private final FilmService service;
    private final FilmMapper mapper;

    protected FilmController(FilmService service, FilmMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/search")
    public List<Film> search(
        @RequestParam(value = "title", required = false) String title,
        @RequestParam(value = "genre", required = false) Genre genre,
        @RequestParam(value = "country", required = false) String country
    ){
        return service.search(title, genre, country);
    }
}
