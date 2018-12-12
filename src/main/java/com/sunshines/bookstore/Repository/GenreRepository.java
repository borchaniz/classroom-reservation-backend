package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Genre;
import org.springframework.data.repository.Repository;

import java.util.List;


public interface GenreRepository extends Repository<Genre, Integer> {
    List<Genre> findAll();
    Genre findById(int id);
}
