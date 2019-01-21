package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findAll();
    List<User> findAllByEmail(String email);

    User findFirstByEmail(String email);
    User findFirstById(int id);
}
