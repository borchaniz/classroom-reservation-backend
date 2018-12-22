package com.sunshines.bookstore.Repository;

import com.sunshines.bookstore.Model.Role;
import com.sunshines.bookstore.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAll();
    Role findFirstByName(String name);
}
