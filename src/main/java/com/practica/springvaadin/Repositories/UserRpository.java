package com.practica.springvaadin.Repositories;

import com.practica.springvaadin.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRpository extends JpaRepository<User, String>{

    @Query("select u from User u where u.email = :email and u.password = :pass")
    User findByEmailAndPassword(@Param("email") String email, @Param("pass") String password);
}
