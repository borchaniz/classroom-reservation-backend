package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Model.Role
import com.sunshines.classrooms.Model.User
import com.sunshines.classrooms.Repository.RoleRepository
import com.sunshines.classrooms.Repository.UserRepository
import com.sunshines.classrooms.Security.JWTTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class   UserController {

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var tokenProvider: JWTTokenProvider

    @PostConstruct
    private fun seedRoles() {
        print("constructed")
        if (roleRepository.findAll().isEmpty()) {
            val admin = Role()
            admin.name = "ADMIN"
            roleRepository.save(admin)

            val user = Role()
            user.name = "user"
            roleRepository.save(user)
        }

        if (userRepository.findAll().isEmpty()) {
            val admin = User()
            var adminRole = roleRepository.findFirstByName("ADMIN")
            if (admin == null) {
                adminRole = Role()
                adminRole.name = "ADMIN"
                roleRepository.save(adminRole)
            }
            admin.role = adminRole
            admin.name = "administrator"
            admin.familyName = "bookstore"
            admin.email = "administrator@classrooms.com"
            admin.setPassword(passwordEncoder!!.encode("159753wtf"))
            admin.phone = 52525252
            userRepository.save(admin)
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    fun register(@Valid @RequestBody user: User): User {
        if (!user.isValid) {
            throw InvalidRequestException("Invalid User Request")
        }
        if (userRepository.findAllByEmail(user.email!!).isNotEmpty())
            throw InvalidRequestException("Email already exists")

        user.role = roleRepository!!.findFirstByName("USER")
        //        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository!!.save(user)
        return user
    }

    @PostMapping("/signin")
    fun authenticateUser(@Valid @RequestBody user: User, response: HttpServletResponse) {

        val authentication = authenticationManager!!.authenticate(
                UsernamePasswordAuthenticationToken(
                        user.email,
                        user.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication
        val jwt = tokenProvider.generateToken(authentication)
        response.addHeader("Authorization", "Bearer $jwt")
        return
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/authenticated")
    fun authenticated(): User {
        return userRepository.findFirstByEmail((SecurityContextHolder.getContext().authentication.principal as User).email!!)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authenticatedAdmin")
    fun authenticatedAdmin(): Any {
        return userRepository.findFirstByEmail((SecurityContextHolder.getContext().authentication.principal as User).email!!)
    }
}
