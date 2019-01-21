package com.sunshines.bookstore.Service;

import com.sunshines.bookstore.Model.User;
import com.sunshines.bookstore.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsSvc implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsSvc(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(username);
//        System.out.print(user.getRole().getAuthority());
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public UserDetails loadUserById(int userId) {
        User user = userRepository.findFirstById(userId);
        if (user == null) {
            throw new UsernameNotFoundException(Integer.toString(userId));
        }
        return user;

    }
}