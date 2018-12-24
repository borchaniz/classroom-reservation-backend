package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.Author;
import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AuthorRepository extends JpaRepository<Author, Integer> {
    List<Author> findAll();
}
