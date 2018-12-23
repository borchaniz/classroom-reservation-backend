package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.CartElement;
import com.sunshines.bookstore.Model.Genre;
import com.sunshines.bookstore.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CartRepository extends JpaRepository<CartElement, Integer> {
    CartElement findFirstByBookAndUser(Book book, User user);
}
