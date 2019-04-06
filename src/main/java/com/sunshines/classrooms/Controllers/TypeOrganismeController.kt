package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Model.Role
import com.sunshines.classrooms.Model.Type_Organisme
import com.sunshines.classrooms.Model.Type_Salle
import com.sunshines.classrooms.Model.User
import com.sunshines.classrooms.Repository.RoleRepository
import com.sunshines.classrooms.Repository.TypeOrganismeRepository
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
@RequestMapping("/type_organisme")
class TypeOrganismeController {

    @Autowired
    lateinit var typeOrganismeRepository: TypeOrganismeRepository


    @GetMapping("")
    fun getAll(): List<Type_Organisme> =
            typeOrganismeRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") type_organisme_id: Int): ResponseEntity<Type_Organisme> {
        return typeOrganismeRepository.findById(type_organisme_id).map { type ->
            ResponseEntity.ok(type)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody type: Type_Organisme): Type_Organisme =
            typeOrganismeRepository.save(type)

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") type_organisme_id: Int,
                          @Valid @RequestBody newType: Type_Organisme): ResponseEntity<Type_Organisme> {

        return typeOrganismeRepository.findById(type_organisme_id).map { existingType ->
            val updatedType: Type_Organisme = existingType
                    .copy(label = newType.label)
            ResponseEntity.ok().body(typeOrganismeRepository.save(updatedType))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") type_organisme_id: Int): ResponseEntity<Void> {

        return typeOrganismeRepository.findById(type_organisme_id).map { type  ->
            typeOrganismeRepository.delete(type)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
