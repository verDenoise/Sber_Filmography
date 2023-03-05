package com.example.filmography.service;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class FilmService extends GenericService<Film> {

    private final FilmRepository repository;

    protected FilmService(FilmRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Film> search(String title, Genre genre, String country) {
        return repository.findAllByGenreOrTitleOrCountry(
                genre,
                title,
                country
        );
    }

    @Override
    public Film update(Film object) {

        return super.update(object);
    }

    public Page<Film> searchByTitle(Pageable pageable, String title) {
        return repository.findAllByTitle(pageable, title);
    }

    public Page<Film> listAllPaginated(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Film> listAllPaginatedForUser(Pageable pageable) {
        return repository.findAllByIsDeletedFalse(pageable);
    }

    public void block(Long id) {
        Film film = getOne(id);
        film.setDeleted(true);
        film.setDeletedBy(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        film.setDeletedWhen(LocalDateTime.now());
        update(film);
    }

    public void unblock(Long id) {
        Film film = getOne(id);
        film.setDeleted(false);
        film.setDeletedWhen(null);
        film.setDeletedBy(null);
        film.setUpdatedBy(((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        film.setUpdatedWhen(LocalDateTime.now());
        update(film);
    }
}
