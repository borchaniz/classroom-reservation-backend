package com.sunshines.bookstore.Model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private int id;

    private String email;

    private String Password;

    private String name;

    private int phone;

    private String privilege;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<CartElement> cart;

    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Operation> operationsHistory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getPrivilege() {
        return privilege;
    }

    public void setPrivilege(String privilege) {
        this.privilege = privilege;
    }

    public List<CartElement> getCart() {
        return cart;
    }

    public void setCart(List<CartElement> cart) {
        this.cart = cart;
    }

    public List<Operation> getOperationsHistory() {
        return operationsHistory;
    }

    public void setOperationsHistory(List<Operation> operationsHistory) {
        this.operationsHistory = operationsHistory;
    }
}
