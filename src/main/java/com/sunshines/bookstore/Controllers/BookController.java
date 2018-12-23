package com.sunshines.bookstore.Controllers;

import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Genre;
import com.sunshines.bookstore.Repository.BookRepository;
import com.sunshines.bookstore.Repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public GenreRepository genreRepository;

    @GetMapping("/")
//    @PreAuthorize("hasRole('SHOPPER')")
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/genre/{id}")
    public List<Book> getBooksByGenre(@PathVariable int id) {
        Genre genre = genreRepository.findById(id);
        return bookRepository.findByGenre(genre);
    }

    @GetMapping("/genre")
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }


    @GetMapping("/{id}")
    public Book getBookById(@PathVariable int id) {
        return bookRepository.findFirstById(id);
    }
}
