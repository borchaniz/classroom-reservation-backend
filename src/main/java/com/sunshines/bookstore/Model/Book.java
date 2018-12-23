package com.sunshines.bookstore.Model;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    private float price;

    @Column(length = 65535, columnDefinition = "Text")
    private String synopsis;

    private Date issuedOn;

    @OneToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @OneToMany
    @JoinColumn(name = "book_id")
    private List<Discount> discounts;

    @Transient
    private Discount activeDiscount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getIssuedOn() {
        return issuedOn;
    }

    public void setIssuedOn(Date issuedOn) {
        this.issuedOn = issuedOn;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

}
