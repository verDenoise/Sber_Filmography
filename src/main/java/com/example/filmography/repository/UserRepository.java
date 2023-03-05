package com.example.filmography.repository;

import com.example.filmography.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends GenericRepository<User> {

    User findUserByLogin(String username);

    User findByEmail(String email);

    User findByChangePasswordToken(String password);

    @Query(nativeQuery = true, value = """
    select * from users where login = :login and is_deleted = false
  """)
    User findUserByLoginAndDeletedFalse(@Param(value = "login") String login);

    User findUserByLoginIs(String loggin);

}
