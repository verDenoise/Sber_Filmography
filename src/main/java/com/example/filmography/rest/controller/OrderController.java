package com.example.filmography.rest.controller;

import com.example.filmography.dto.OrderDto;
import com.example.filmography.dto.PurchaseFilmDto;
import com.example.filmography.dto.RentFilmDto;
import com.example.filmography.mapper.OrderMapper;
import com.example.filmography.model.Order;
import com.example.filmography.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/rest/order")
public class OrderController extends GenericController<Order, OrderDto> {

    private final OrderService service;
    private final OrderMapper mapper;

    protected OrderController(OrderService service, OrderMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("rent-film")
    public OrderDto rentFilm(@RequestBody RentFilmDto rentFilmDto) {
        return mapper.toDto(service.rentFilm(rentFilmDto));
    }

    @PostMapping("purchase-film")
    public OrderDto purchaseFilm(@RequestBody PurchaseFilmDto purchaseFilmDto) {
        return mapper.toDto(service.purchaseFilm(purchaseFilmDto));
    }

    @GetMapping("user-orders/{userId}")
    public List<OrderDto> getUserOrders(@PathVariable Long userId){
        return mapper.toDtos(service.getUserOrders(userId));
    }
}

