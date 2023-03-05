package com.example.filmography.repository;

import com.example.filmography.model.Director;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface DirectorRepository extends GenericRepository<Director> {
    Set<Director> findAllByIdIn(Set<Long> ids);

    Page<Director> findAllByDirectorFIO(Pageable pageable, String directorFIO);

    Page<Director> findAllByIsDeletedFalse(Pageable pageable);

    List<Director> findDirectorsByDirectorFIOLike(String directorFIO);

}
