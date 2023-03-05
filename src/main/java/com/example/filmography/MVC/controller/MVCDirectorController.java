package com.example.filmography.MVC.controller;

import com.example.filmography.dto.AddFilmDto;
import com.example.filmography.dto.DirectorDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.mapper.DirectorWithFilmsMapper;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@Slf4j
@RequestMapping("/directors")
public class MVCDirectorController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;
    private final DirectorWithFilmsMapper directorWithFilmsMapper;
    private final DirectorService service;
    private final DirectorMapper mapper;

    public MVCDirectorController(FilmService filmService, FilmMapper filmMapper, DirectorWithFilmsMapper directorWithFilmsMapper, DirectorService service, DirectorMapper mapper) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
        this.directorWithFilmsMapper = directorWithFilmsMapper;
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Direction.ASC, "directorFIO"));
        Page<Director> directorPage = null;
        if (customUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
            directorPage = service.listAllPaginatedForUser(pageRequest);
        } else {
            directorPage = service.listAllPaginated(pageRequest);
        }
        List<DirectorDto> directorDtos = directorPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("directors", new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));
        return "directors/viewAllDirectors";
    }

    @GetMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDto directorDTO) {
        return "/directors/addDirector";
    }

    @PostMapping("/add")
    public String create(
            @ModelAttribute("directorForm") @Valid DirectorDto directorDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "/directors/addDirector";
        } else {
            service.create(mapper.toEntity(directorDTO));
            return "redirect:/directors";
        }

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/directors";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable Long id, Model model) {
        model.addAttribute("director", mapper.toDto(service.getOne(id)));
        return "directors/updateDirector"; //путь до html файла
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("directorForm") DirectorDto directorDto) {
        Director director = service.getOne(directorDto.getId());
        director.setDirectorFIO(directorDto.getDirectorFIO());
        director.setPosition(directorDto.getPosition());
        service.update(director);
        return "redirect:/directors";
    }

    @PostMapping("/search")
    public String searchByDirectorFIO(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            Model model, @ModelAttribute("searchDirectors") DirectorDto directorDto
    ) {
        if (directorDto.getDirectorFIO().trim().equals("")) {
            model.addAttribute("directors", mapper.toDtos(service.listAll()));
        } else {
            PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Direction.ASC, "directorFIO"));
            Page<Director> directorPage = service.searchByDirectorFIO(pageRequest, directorDto.getDirectorFIO());
            List<DirectorDto> directorDtos = directorPage
                    .stream()
                    .map(mapper::toDto)
                    .toList();
            model.addAttribute("directors", new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));
        }
        return "directors/viewAllDirectors";
    }


    @GetMapping("/add-film/{directorId}")
    public String addFilm(Model model, @PathVariable Long directorId) {
        model.addAttribute("films", filmMapper.toDtos(filmService.listAll()));
        model.addAttribute("directorId", directorId);
        model.addAttribute("director", mapper.toDto(service.getOne(directorId)).getDirectorFIO());
        return "directors/addDirectorFilm";
    }

    @PostMapping("/add-film")
    public String addFilm(@ModelAttribute("directorFilmForm") AddFilmDto addFilmDto) {
        service.addFilm(addFilmDto);
        return "redirect:/directors";
    }

    @GetMapping("/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorWithFilmsMapper.toDto(service.getOne(id)));
        return "directors/viewDirector";
    }

    @GetMapping("/block/{id}")
    public String block(@PathVariable Long id) {
        service.block(id);
        return "redirect:/directors";
    }

    @GetMapping("/unblock/{id}")
    public String unblock(@PathVariable Long id) {
        service.unblock(id);
        return "redirect:/directors";
    }

}
