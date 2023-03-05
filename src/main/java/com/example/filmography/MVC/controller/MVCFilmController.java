package com.example.filmography.MVC.controller;

import com.example.filmography.dto.DirectorDto;
import com.example.filmography.dto.FilmDto;
import com.example.filmography.dto.FilmWithDirectorsDto;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.mapper.FilmWithDirectorsMapper;
import com.example.filmography.model.Director;
import com.example.filmography.model.Film;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/films")
public class MVCFilmController {

    private final FilmService service;

    private final FilmMapper mapper;

    private final FilmWithDirectorsMapper filmWithDirectorsMapper;

    public MVCFilmController(FilmService service, FilmMapper mapper, FilmWithDirectorsMapper filmWithDirectorsMapper) {
        this.service = service;
        this.mapper = mapper;
        this.filmWithDirectorsMapper = filmWithDirectorsMapper;
    }


    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<Film> filmPage = null;
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            filmPage = service.listAllPaginatedForUser(pageRequest);
        } else {
            filmPage = service.listAllPaginated(pageRequest);
        }
        List<FilmWithDirectorsDto> filmDtos = filmPage
                .stream()
                .map(filmWithDirectorsMapper::toDto)
                .toList();
        model.addAttribute("films", new PageImpl<>(filmDtos, pageRequest, filmPage.getTotalElements()));
        return "films/viewAllFilms";
    }


    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmsForm") FilmDto filmDto) {
        service.create(mapper.toEntity(filmDto));
        return "redirect:/films";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/films";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmWithDirectorsMapper.toDto(service.getOne(id)));
        return "films/updateFilm"; //путь до html файла
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("filmForm") FilmDto filmDto) {
        Film film = service.getOne(filmDto.getId());
        film.setTitle(filmDto.getTitle());
        film.setGenre(filmDto.getGenre());
        film.setCountry(filmDto.getCountry());
        film.setPremierYear(filmDto.getPremierYear());
        film.setAmount(filmDto.getAmount());
        service.update(film);
        return "redirect:/films";
    }

    @PostMapping("/search")
    public String searchByTitle(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model, @ModelAttribute("searchFilms") FilmDto filmDto
    ) {
        if (filmDto.getTitle().trim().equals("")) {
            model.addAttribute("films", mapper.toDtos(service.listAll()));
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
            Page<Film> filmPage = service.searchByTitle(pageRequest, filmDto.getTitle());
            List<FilmWithDirectorsDto> filmDtos = filmPage
                    .stream()
                    .map(filmWithDirectorsMapper::toDto)
                    .toList();
            model.addAttribute("films", new PageImpl<>(filmDtos, pageRequest, filmPage.getTotalElements()));
        }
        return "films/viewAllFilms";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("film", filmWithDirectorsMapper.toDto(service.getOne(id)));
        return "films/viewFilm";
    }

    @GetMapping("/block/{id}")
    public String blockFilm(@PathVariable Long id) {
        service.block(id);
        return "redirect:/films";
    }

    @GetMapping("/unblock/{id}")
    public String unblockFilm(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/films";
    }

}
