package com.example.filmography.mapper;

import com.example.filmography.dto.FilmWithDirectorsDto;
import com.example.filmography.model.Film;
import com.example.filmography.model.GenericModel;
import com.example.filmography.repository.DirectorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FilmWithDirectorsMapper extends GenericMapper<Film , FilmWithDirectorsDto> {

    private final ModelMapper mapper;
    private final DirectorRepository directorRepository;


    public FilmWithDirectorsMapper(ModelMapper mapper, DirectorRepository directorRepository) {
        super(mapper, Film.class, FilmWithDirectorsDto.class);
        this.mapper = mapper;
        this.directorRepository = directorRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Film.class, FilmWithDirectorsDto.class)
                .addMappings(m -> m.skip(FilmWithDirectorsDto::setDirectorsIds)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(FilmWithDirectorsDto.class, Film.class)
                .addMappings(m -> m.skip(Film::setDirectors)).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(FilmWithDirectorsDto source, Film destination) {
        destination.setDirectors(directorRepository.findAllByIdIn(source.getDirectorsIds()));
    }

    @Override
    void mapSpecificFields(Film source, FilmWithDirectorsDto destination) {
        destination.setDirectorsIds(getIds(source));
    }

    private Set<Long> getIds(Film film) {
        return Objects.isNull(film) || Objects.isNull(film.getId())
                ? null
                : film.getDirectors().stream()
                .map(GenericModel::getId)
                .collect(Collectors.toSet());
    }

}
