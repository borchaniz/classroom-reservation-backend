package com.sunshines.bookstore.Controllers;


import com.sunshines.bookstore.Model.Author;
import com.sunshines.bookstore.Model.Book;
import com.sunshines.bookstore.Model.Genre;
import com.sunshines.bookstore.Repository.AuthorRepository;
import com.sunshines.bookstore.Repository.BookRepository;
import com.sunshines.bookstore.Repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    public AuthorRepository authorRepository;

    @GetMapping
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Author> addAuthor(@RequestBody @Valid Author author){
        this.authorRepository.save(author);
        return this.getAllAuthors();
    }
}
