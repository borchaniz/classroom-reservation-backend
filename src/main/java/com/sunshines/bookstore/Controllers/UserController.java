package com.sunshines.bookstore.Controllers;

import com.sunshines.bookstore.Security.JWTTokenProvider;
import com.sunshines.bookstore.Exception.InvalidRequestException;
import com.sunshines.bookstore.Model.Book;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    public AuthenticationManager authenticationManager;

    @Autowired
    public JWTTokenProvider tokenProvider;

    @PostConstruct
    private void seedRoles() {
        System.out.print("constructed");
        if (roleRepository.findAll().size() == 0) {
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);

            Role shopper = new Role();
            shopper.setName("SHOPPER");
            roleRepository.save(shopper);
        }

        if (userRepository.findAll().size() == 0) {
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
    public User register(@Valid @RequestBody User user) {
        if (!user.isValid()) {
            throw new InvalidRequestException("Invalid User Request");
        }
        if (userRepository.findAllByEmail(user.getEmail()).size() != 0)
            throw new InvalidRequestException("Email already exists");

        user.setRole(roleRepository.findFirstByName("SHOPPER"));
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return user;
    }

    @PostMapping("/signin")
    public void authenticateUser(@Valid @RequestBody User user, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        response.addHeader("Authorization", "Bearer "+jwt);
        return;
    }


    @PreAuthorize("hasRole('SHOPPER')")
    @GetMapping("/authenticated")
    public User authenticated() {
        User user = userRepository.findFirstByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        user.calculateTotalCart();
        return user;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authenticatedAdmin")
    public Object authenticatedAdmin() {
        User user = userRepository.findFirstByEmail(((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getEmail());
        return user;
    }

    
    @PostMapping("/addToCart")
    @PreAuthorize("hasRole('SHOPPER')")
    public void addTocart(@RequestBody @Valid AddToCartRequest request) {
        CartElement element;
        User user = authenticated();
        Book book = bookRepository.findFirstById(request.getBookId());
        if (book==null){
            throw new InvalidRequestException("Book Not found");
        }
        element = cartRepository.findFirstByBookAndUser(book, user);
        if (element == null) {
            element = new CartElement();
            element.setQuantity(0);
        }
        element.setBook(book);
        element.setQuantity(element.getQuantity() + request.getQuantity());
        element.setUser(user);
        cartRepository.save(element);
    }

    @DeleteMapping("/clearCart")
    @PreAuthorize("hasRole('SHOPPER')")
    public User clearCart() {
        User user = this.authenticated();
        for (CartElement item : user.getCart()) {
            this.cartRepository.delete(item);
        }
        return authenticated();
    }
}
