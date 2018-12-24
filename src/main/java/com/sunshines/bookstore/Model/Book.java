package com.sunshines.bookstore.Model;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue
    private int id;

    private String title;

    private float price;

    private String image;

    @Column(length = 65535, columnDefinition = "Text")
    private String synopsis;

    @OneToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @OneToMany
    @JoinColumn(name = "book_id")
    private List<Discount> discounts;

    @Transient
    private Discount activeDiscount;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

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

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Discount getActiveDiscount() {
        return activeDiscount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setActiveDiscount(Discount activeDiscount) {
        this.activeDiscount = activeDiscount;
    }

    public void setBestDiscount() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        activeDiscount = null;
        for (Discount d : discounts) {
            if (now.compareTo(d.getStartDate()) > 0 && now.compareTo(d.getEndDate()) < 0 && (activeDiscount == null || activeDiscount.getPercentage() < d.getPercentage())) {
                activeDiscount = d;
            }
        }
    }

}
