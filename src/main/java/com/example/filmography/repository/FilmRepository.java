package com.example.filmography.repository;

import com.example.filmography.model.Film;
import com.example.filmography.model.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FilmRepository extends GenericRepository<Film> {
    Set<Film> findAllByIdIn(Set<Long> ids);

    List<Film> findAllByTitleOrGenreOrCountry(String title, Genre genre, String country);

    @Query(value = """
            select vt.films_id
            from (select films_id
                       , count(directors_id) cc
                  from films_directors df
                  where df.films_id IN :filmsId
                  GROUP BY films_id) as vt
            where vt.cc = 1
                """,
            nativeQuery = true)
    List<Long> findAllFilmsWhereDirectorIsOne(@Param(value = "filmsId") List<Long> filmsId);

    @Query(value = """
            select films_id
            from films_directors df
                     INNER JOIN orders o
                                ON df.films_id = o.film_id
            where df.directors_id = :directorId
            And df.films_id IN :filmsId
            And o.returned = :returned
             """,
            nativeQuery = true)
    List<Long> filmsIsReturned(@Param(value = "directorId") Long directorId,
                               @Param(value = "filmsId") List<Long> filmsId,
                               @Param(value = "returned") boolean returned);

    @Query(value = """
            select o.id
            from films_directors df
                     INNER JOIN orders o
                                ON df.films_id = o.film_id
            where df.directors_id = :directorId
            And df.films_id IN :filmsId
            And o.returned != false
            """,
            nativeQuery = true)
    List<Long> getOrdersByFilmsToDelete(@Param(value = "directorId") Long directorId,
                                        @Param(value = "filmsId") List<Long> filmsId);

    List<Film> findFilmsByDirectorsId(Long id);

    Page<Film> findAllByTitle(Pageable pageable, String title);

    Page<Film> findAllByIsDeletedFalse(Pageable pageable);

    List<Film> findAllByGenreOrTitleOrCountry(Genre genre, String title, String country);

    @Query(value = """
            select *
            from films
            order by random()
            limit 1     
              """,
            nativeQuery = true)
    Film getRandomFilm();


}
