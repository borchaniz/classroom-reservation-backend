package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAll();
    List<Book> findByGenre(Genre genre);
}
