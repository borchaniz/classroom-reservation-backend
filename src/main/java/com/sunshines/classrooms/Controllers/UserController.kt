package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Model.Role
import com.sunshines.classrooms.Model.User
import com.sunshines.classrooms.Repository.RoleRepository
import com.sunshines.classrooms.Repository.UserRepository
import com.sunshines.classrooms.Security.JWTTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
class UserController {

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
            user.name = "USER"
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
            admin.first_name = "admin"
            admin.last_name = "admin"
            admin.cin = "00700707"
            admin.email = "administrator@classrooms.com"
            admin.setPassword(passwordEncoder.encode("12345678"))
            admin.phone = "51515151"
            userRepository.save(admin)
        }
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    fun registerStudent(@Valid @RequestBody user: User): User {
        if (!user.isValid) {
            throw InvalidRequestException("Invalid User Request")
        }
        if (userRepository.findAllByEmail(user.email!!).isNotEmpty())
            throw InvalidRequestException("Email already exists")
        user.role = roleRepository.findFirstByName("USER")
        user.setPassword(passwordEncoder.encode(user.password))
        userRepository.save(user)
        return user
    }

    @PostMapping("/signin")
    fun authenticateUser(@RequestBody user: User, response: HttpServletResponse) {
        val authentication = authenticationManager.authenticate(
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

    @PutMapping("/validate/{id}")
    fun validateUser(@PathVariable(value = "id") user_id: Int): ResponseEntity<User> {
        return userRepository.findById(user_id).map { existingUser ->
            val validatedUser: User = existingUser
                    .copy(status = 1)
            ResponseEntity.ok().body(userRepository.save(validatedUser))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable(value = "id") user_id: Int): User {
        val user = userRepository.findById(user_id)
        userRepository.delete(user.get())
        return user.get()

    }

    @PutMapping("/unValidateUser/{id}")
    fun unValidateUser(@PathVariable(value = "id") user_id: Int): ResponseEntity<User> {

        return userRepository.findById(user_id).map { existingUser ->
            val unValidatedUser: User = existingUser
                    .copy(status = 2)
            ResponseEntity.ok().body(userRepository.save(unValidatedUser))
        }.orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("")
//    @PreAuthorize("hasAuthority('ADMIN')")
    fun getAllUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok().body(userRepository.findAll().filter { user -> user.role!!.name != "ADMIN" })
    }


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/authenticated")
    fun authenticated(): User {
        return userRepository.findFirstByEmail((SecurityContextHolder.getContext().authentication.principal as User).email!!)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/authenticatedAdmin")
    fun authenticatedAdmin(): User {
        return userRepository.findFirstByEmail((SecurityContextHolder.getContext().authentication.principal as User).email!!)
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/unvalidated")
    fun unvalidatedUsers(): List<User> {
        return userRepository.findAllByStatus(0).filter { it.role?.name.orEmpty() != "ADMIN" }
    }

}
