package com.sunshines.bookstore.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private int id;

    private String email;

    @NotBlank
    private String password;

    @Transient
    private String cPassword;

    private String name;

    @Column(name = "family_name")
    private String familyName;

    private int phone;

    @Transient
    private float totalCart;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getcPassword() {
        return cPassword;
    }

    public void setcPassword(String cPassword) {
        this.cPassword = cPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }


    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isValid() {
        if (!this.password.equals(this.cPassword) || this.name.equals("") || this.familyName.equals("")) return false;
        return true;
    }

    public float getTotalCart() {
        return totalCart;
    }

    public void setTotalCart(float totalCart) {
        this.totalCart = totalCart;
    }

    public void calculateTotalCart(){
        totalCart = 0;
        for(CartElement element : cart){
            totalCart+=element.getQuantity()*element.getBook().getPrice();
        }
    }


}
