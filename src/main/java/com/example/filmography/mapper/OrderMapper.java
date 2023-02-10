package com.example.filmography.mapper;

import com.example.filmography.dto.OrderDto;
import com.example.filmography.model.Order;
import com.example.filmography.repository.FilmRepository;
import com.example.filmography.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class OrderMapper extends GenericMapper<Order, OrderDto> {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    protected OrderMapper(ModelMapper mapper, FilmRepository filmRepository, UserRepository userRepository) {
        super(mapper, Order.class, OrderDto.class);
        this.filmRepository = filmRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        super.mapper.createTypeMap(Order.class, OrderDto.class)
                .addMappings(m -> m.skip(OrderDto::setUserId)).setPostConverter(toDtoConverter())
                .addMappings(m -> m.skip(OrderDto::setFilmId)).setPostConverter(toDtoConverter());
    }

    @Override
    void mapSpecificFields(OrderDto source, Order destination) {
        destination.setFilm(filmRepository.findById(source.getFilmId()).orElseThrow());
        destination.setUser(userRepository.findById(source.getUserId()).orElseThrow());
    }

    @Override
    void mapSpecificFields(Order source, OrderDto destination) {
        destination.setUserId(source.getUser().getId());
        destination.setFilmId(source.getFilm().getId());
    }

}
