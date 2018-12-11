package com.sunshines.bookstore.Model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue
    private int id;

    private int quantity;

    private Timestamp date;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
