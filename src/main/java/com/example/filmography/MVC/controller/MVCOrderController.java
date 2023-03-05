package com.example.filmography.MVC.controller;

import com.example.filmography.dto.OrderDto;
import com.example.filmography.dto.PurchaseFilmDto;
import com.example.filmography.dto.RentFilmDto;
import com.example.filmography.dto.UserDto;
import com.example.filmography.mapper.FilmMapper;
import com.example.filmography.mapper.OrderMapper;
import com.example.filmography.model.Order;
import com.example.filmography.model.User;
import com.example.filmography.service.FilmService;
import com.example.filmography.service.OrderService;
import com.example.filmography.service.userDetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class MVCOrderController {

    private final FilmService filmService;
    private final FilmMapper filmMapper;
    private final OrderMapper mapper;
    private final OrderService service;

    public MVCOrderController(FilmService filmService, FilmMapper filmMapper, OrderMapper mapper, OrderService service) {
        this.filmService = filmService;
        this.filmMapper = filmMapper;
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping("/get-film/{filmId}")
    public String getFilm(@PathVariable Long filmId, Model model) {
        model.addAttribute("film", filmMapper.toDto(filmService.getOne(filmId)));
        return "userFilm/getFilm";
    }

    @PostMapping("/get-film")
    public String getFilm(@ModelAttribute("orderForm") RentFilmDto rentFilmDto) {
        if (rentFilmDto.getRentPeriod() < 1 || rentFilmDto.getRentPeriod() > 365 || rentFilmDto.getRentPeriod() == null){
            return "redirect:/order/get-film/"+rentFilmDto.getFilmId()+"/?error=Invalid Number of Rental Days";
        }
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        rentFilmDto.setUserId(Long.valueOf(customUserDetails.getUserId()));
        service.rentFilm(rentFilmDto);
        return "redirect:/films";
    }

    @GetMapping("/purchase-film/{filmId}")
    public String purchaseFilm(@PathVariable Long filmId) {
        PurchaseFilmDto purchaseFilmDto = new PurchaseFilmDto();
        purchaseFilmDto.setFilmId(filmId);
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        purchaseFilmDto.setUserId(Long.valueOf(customUserDetails.getUserId()));
        service.purchaseFilm(purchaseFilmDto);
        return "redirect:/films";

    }

    @GetMapping("/user-films/{id}")
    public String userFilms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @PathVariable Long id,
            Model model
    ) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<Order> orderPage = service.listAllPaginated(pageRequest);
        List<OrderDto> orderDtos = orderPage
                .stream()
                .map(mapper::toDto)
                .toList();
        model.addAttribute("orders", new PageImpl<>(orderDtos, pageRequest, orderPage.getTotalElements()));
        model.addAttribute("UserID", id);
        return "userFilm/viewAllUserFilms";
    }

    @GetMapping("/return-film/{id}")
    public String returnFilm(@PathVariable Long id) {
        service.returnFilm(id);
        return "redirect:/order/user-films/" + service.getOne(id).getUser().getId();
    }

}
