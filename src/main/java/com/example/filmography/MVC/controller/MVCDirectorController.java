package com.example.filmography.MVC.controller;

import com.example.filmography.dto.DirectorDto;
import com.example.filmography.mapper.DirectorMapper;
import com.example.filmography.model.Director;
import com.example.filmography.service.DirectorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/directors")
public class MVCDirectorController {

    private final DirectorService service;
    private final DirectorMapper mapper;

    public MVCDirectorController(DirectorService service, DirectorMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("")
    public String getAll(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "5") int pageSize,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "directorFIO"));
        Page<Director> directorPage = service.listAllPaginated(pageRequest);
        List<DirectorDto> directorDtos = directorPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("directors", new PageImpl<>(directorDtos, pageRequest, directorPage.getTotalElements()));
        return "directors/viewAllDirectors";
    }


    @GetMapping("/add")
    public String create() {
        return "directors/addDirector";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("directorsForm") DirectorDto directorsDto) {
        service.create(mapper.toEntity(directorsDto));
        return "redirect:/directors";
    }


}
