package com.sunshines.classrooms.Service

import com.sunshines.classrooms.Model.User
import com.sunshines.classrooms.Repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsSvc : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): User {
//        System.out.print(user.getRole().getAuthority());
        return userRepository.findFirstByEmail(username) ?: throw UsernameNotFoundException(username)
    }

    fun loadUserById(userId: Int): UserDetails {
        return userRepository.findFirstById(userId) ?: throw UsernameNotFoundException(Integer.toString(userId))

    }
}