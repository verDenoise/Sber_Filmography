package com.example.filmography.service;

import com.example.filmography.dto.PurchaseFilmDto;
import com.example.filmography.dto.RentFilmDto;
import com.example.filmography.model.Film;
import com.example.filmography.model.Order;
import com.example.filmography.model.User;
import com.example.filmography.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService extends GenericService<Order> {

    private final OrderRepository repository;
    private final UserService userService;
    private final FilmService filmService;

    protected OrderService(OrderRepository repository, UserService userService, FilmService filmService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
        this.filmService = filmService;
    }

    public Order rentFilm(RentFilmDto rentFilmDto){
        User user = userService.getOne(rentFilmDto.getUserId());
        Film film = filmService.getOne(rentFilmDto.getFilmId());

        Order order = Order.builder()
                .rentDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now().plusDays(rentFilmDto.getRentPeriod()))
                .rentPeriod(rentFilmDto.getRentPeriod())
                .returned(false)
                .purchase(false)
                .amount(rentFilmDto.getAmount())
                .user(user)
                .film(film)
                .build();
        return repository.save(order);
    }

    public Order purchaseFilm(PurchaseFilmDto purchaseFilmDto){
        User user = userService.getOne(purchaseFilmDto.getUserId());
        Film film = filmService.getOne(purchaseFilmDto.getFilmId());

        Order order = Order.builder()
                .rentDate(LocalDateTime.now())
                .returnDate(LocalDateTime.now())
                .rentPeriod(0)
                .returned(false)
                .purchase(true)
                .amount(purchaseFilmDto.getAmount())
                .user(user)
                .film(film)
                .build();
        return repository.save(order);
    }

    public List<Order> getUserOrders(Long userID){
        return userService.getOne(userID).getOrder().stream().toList();//это плохо или нет?
    }

}
