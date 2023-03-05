package com.example.filmography.service;

import com.example.filmography.dto.AddFilmDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.repository.DirectorRepository;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.repository.OrderRepository;
import com.example.filmography.service.userDetails.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class DirectorService extends GenericService<Director> {

    private final DirectorRepository directorRepository;
    private final FilmService filmService;
    private final FilmRepository filmRepository;
    private final OrderRepository orderRepository;

    protected DirectorService(DirectorRepository directorRepository, FilmService filmService, FilmRepository filmRepository, OrderRepository orderRepository) {
        super(directorRepository);
        this.directorRepository = directorRepository;
        this.filmService = filmService;
        this.filmRepository = filmRepository;
        this.orderRepository = orderRepository;
    }

    public Director addFilm(AddFilmDto addFilmDto) {
        Director director = getOne(addFilmDto.getDirectorId());
        Film film = filmService.getOne(addFilmDto.getFilmId());
        director.getFilms().add(film);
        return update(director);
    }

    @Override
    public void delete(Long id) {

        List<Film> films = filmRepository.findFilmsByDirectorsId(id);
        if(films.isEmpty()) {
            directorRepository.deleteById(id);
            return;
        }
        List<Long> filmsIds = films.stream().map(Film::getId).toList();

        List<Long> filmsWithDirector = filmRepository.findAllFilmsWhereDirectorIsOne(filmsIds);
        if (filmsWithDirector.isEmpty()) {
            throw new UnsupportedOperationException("Cannot delete director, the contestant is present in the film(s) with other directors");
        }

        List<Long> filmsIsReturned = filmRepository.filmsIsReturned(id, filmsWithDirector, false);
        if(filmsIsReturned.isEmpty()){
            filmsIsReturned = filmRepository.filmsIsReturned(id, filmsWithDirector, true);
            if(!filmsIsReturned.isEmpty()){
                List<Long> orderListIds = filmRepository.getOrdersByFilmsToDelete(id, filmsIsReturned);
                for (Long orderId : orderListIds) {
                    orderRepository.findById(orderId).get().setFilm(null);
                }
                for (Long idFilm: filmsIsReturned) {
                    filmRepository.deleteById(idFilm);
                }
            }else{
                directorRepository.deleteById(id);
            }
        }else{
            throw new UnsupportedOperationException("Cannot delete director, where film is rent");
        }


    }

    public Page<Director> listAllPaginated(PageRequest pageRequest) {
        return  directorRepository.findAll(pageRequest);
    }

    public Page<Director> searchByDirectorFIO(Pageable pageable, String directorFIO) {
        return directorRepository.findAllByDirectorFIO(pageable, directorFIO);
    }

    public void block(Long id) {
        Director director = getOne(id);
        director.setDeleted(true);
        director.setDeletedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        director.setDeletedWhen(LocalDateTime.now());
        update(director);
    }

    public void unblock(Long id) {
        Director director = getOne(id);
        director.setDeleted(false);
        director.setDeletedBy(null);
        director.setDeletedWhen(null);
        director.setUpdatedBy(((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        director.setUpdatedWhen(LocalDateTime.now());
        update(director);
    }

    public Page<Director> listAllPaginatedForUser(Pageable pageable) {
        return directorRepository.findAllByIsDeletedFalse(pageable);
    }

}
