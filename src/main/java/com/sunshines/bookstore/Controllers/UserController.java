package com.sunshines.bookstore.Controllers;

import com.sunshines.bookstore.Exception.InvalidRequestException;
import com.sunshines.bookstore.Model.CartElement;
import com.sunshines.bookstore.Model.Request.AddToCartRequest;
import com.sunshines.bookstore.Model.Role;
import com.sunshines.bookstore.Model.User;
import com.sunshines.bookstore.Repository.BookRepository;
import com.sunshines.bookstore.Repository.CartRepository;
import com.sunshines.bookstore.Repository.RoleRepository;
import com.sunshines.bookstore.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public CartRepository cartRepository;

    @PostConstruct
    private void seedRoles() {
        System.out.print("constructed");
        if (roleRepository.findAll().size() == 0){
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Role shopper = new Role();
            shopper.setName("SHOPPER");
            roleRepository.save(shopper);
        }

        if (userRepository.findAll().size() == 0){
            User admin = new User();
            Role adminRole = roleRepository.findFirstByName("ADMIN");
            if (admin == null) {
                adminRole = new Role();
                adminRole.setName("ADMIN");
                roleRepository.save(adminRole);
            }
            admin.setRole(adminRole);
            admin.setName("administrator");
            admin.setFamilyName("bookstore");
            admin.setEmail("administrator@bookstore.com");
            admin.setPassword(passwordEncoder.encode("159753wtf"));
            admin.setPhone(52525252);
            userRepository.save(admin);
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public User register(@Valid @RequestBody User user){
        if (!user.isValid()){
            throw new InvalidRequestException("Invalid User Request");
        }
        if (userRepository.findAllByEmail(user.getEmail()).size()!=0)
            throw new InvalidRequestException("Email already exists");

        user.setRole(roleRepository.findFirstByName("SHOPPER"));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @GetMapping("/authenticated")
    public User authenticated(){
        return userRepository.findFirstByEmail(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
    }

    @PostMapping("/addToCart")
    public void addTocart(@RequestBody @Valid AddToCartRequest request){
        CartElement element = new CartElement();
        element.setBook(bookRepository.findFirstById(request.getBookId()));
        element.setQuantity(request.getQuantity());
        element.setUser(authenticated());
        cartRepository.save(element);

    }
}
