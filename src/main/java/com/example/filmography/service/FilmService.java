package com.example.filmography.service;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.repository.DirectorRepository;
import com.example.filmography.repository.FilmRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService extends GenericService<Film> {

    private final FilmRepository filmRepository;
    private final DirectorRepository directorRepository;

    protected FilmService(FilmRepository filmRepository, DirectorRepository directorRepository) {
        super(filmRepository);
        this.filmRepository = filmRepository;
        this.directorRepository = directorRepository;
    }

    public List<Film> search(String title, Genre genre, String country) {
        return filmRepository.findAllByTitleOrGenreOrCountry(title, genre, country);
    }

    public Page<Film> listAllPaginated(PageRequest pageRequest) {
        return filmRepository.findAll(pageRequest);
    }
}
