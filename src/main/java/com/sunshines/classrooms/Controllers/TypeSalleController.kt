package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Model.Role
import com.sunshines.classrooms.Model.Type_Salle
import com.sunshines.classrooms.Model.User
import com.sunshines.classrooms.Repository.RoleRepository
import com.sunshines.classrooms.Repository.TypeSalleRepository
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
import java.util.*

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/type_salle")
class TypeSalleController {

    @Autowired
    lateinit var typeSalleRepository: TypeSalleRepository


    @GetMapping("")
    fun getAll(): List<Type_Salle> =
            typeSalleRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") type_salle_id: Int): ResponseEntity<Type_Salle> {
        return typeSalleRepository.findById(type_salle_id).map { type ->
            ResponseEntity.ok(type)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody type: Type_Salle): Type_Salle =
            typeSalleRepository.save(type)

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") type_salle_id: Int,
                          @Valid @RequestBody newType: Type_Salle): ResponseEntity<Type_Salle> {

        return typeSalleRepository.findById(type_salle_id).map { existingType ->
            val updatedType: Type_Salle = existingType
                    .copy(label = newType.label)
            ResponseEntity.ok().body(typeSalleRepository.save(updatedType))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") type_salle_id: Int): ResponseEntity<Void> {

        return typeSalleRepository.findById(type_salle_id).map { type  ->
            typeSalleRepository.delete(type)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
