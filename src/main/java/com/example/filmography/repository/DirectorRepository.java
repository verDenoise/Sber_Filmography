package com.example.filmography.repository;

import com.example.filmography.model.Director;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface DirectorRepository extends GenericRepository<Director> {
    Set<Director> findAllByIdIn(Set<Long> ids);
}
