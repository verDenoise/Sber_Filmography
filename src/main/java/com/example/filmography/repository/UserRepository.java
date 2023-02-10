package com.example.filmography.repository;

import com.example.filmography.model.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends GenericRepository<User> {

    User findUserByLogin(String username);
}
