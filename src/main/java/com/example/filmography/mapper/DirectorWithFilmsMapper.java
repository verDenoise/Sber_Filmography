package com.example.filmography.mapper;

import com.example.filmography.dto.DirectorWithFilmsDto;
import com.example.filmography.model.Director;
import com.example.filmography.model.GenericModel;
import com.example.filmography.repository.FilmRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DirectorWithFilmsMapper extends GenericMapper<Director, DirectorWithFilmsDto>{

    private final ModelMapper mapper;
    private final FilmRepository filmRepository;

    protected DirectorWithFilmsMapper(ModelMapper mapper, FilmRepository filmRepository) {
        super(mapper, Director.class, DirectorWithFilmsDto.class);
        this.mapper = mapper;
        this.filmRepository = filmRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Director.class, DirectorWithFilmsDto.class)
                .addMappings(m -> m.skip(DirectorWithFilmsDto::setFilmsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(DirectorWithFilmsDto.class, Director.class)
                .addMappings(m -> m.skip(Director::setFilms)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(DirectorWithFilmsDto source, Director destination) {
        destination.setFilms(filmRepository.findAllByIdIn(source.getFilmsIds()));
    }

    @Override
    void mapSpecificFields(Director source, DirectorWithFilmsDto destination) {
        destination.setFilmsIds(getIds(source));
    }

    private Set<Long> getIds(Director director) {
        return Objects.isNull(director) || Objects.isNull(director.getId())
                ? null
                : director.getFilms().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }

}
