package com.example.gurbocoursework.Repositories;

import com.example.gurbocoursework.Model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
