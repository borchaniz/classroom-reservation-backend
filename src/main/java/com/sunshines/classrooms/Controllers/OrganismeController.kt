package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Exception.NotFoundException
import com.sunshines.classrooms.Model.*
import com.sunshines.classrooms.Repository.OrganismeRepository
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
import java.util.function.Supplier

import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/organisme")
class OrganismeController {

    @Autowired
    lateinit var organismeRepository: OrganismeRepository

    @Autowired
    lateinit var typeOrganismeRepository: TypeOrganismeRepository


    @GetMapping("")
    fun getAll(): List<Organisme> =
            organismeRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") organisme_id: Int): ResponseEntity<Organisme> {
        return organismeRepository.findById(organisme_id).map { organisme ->
            ResponseEntity.ok(organisme)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody organisme: Organisme): Organisme {
            organisme.type = typeOrganismeRepository.findById(organisme.type_id!!).orElse(null)

            if(organisme.type == null)
                throw NotFoundException("type organisme not found")
            return organismeRepository.save(organisme)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") organisation_id: Int,
                          @Valid @RequestBody newOrganisme: Organisme): ResponseEntity<Organisme> {
        if(newOrganisme.type_id != null) {
            newOrganisme.type = typeOrganismeRepository.findById(newOrganisme.type_id!!).orElse(null)

            if (newOrganisme.type == null)
                throw NotFoundException("type organisme not found")
        }
        return organismeRepository.findById(organisation_id).map { existingOrganisme ->
            val updatedOrganisme: Organisme = existingOrganisme
                    .copy(label = newOrganisme.label)
            ResponseEntity.ok().body(organismeRepository.save(updatedOrganisme))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") organisation_id: Int): ResponseEntity<Void> {

        return organismeRepository.findById(organisation_id).map { organisme  ->
            organismeRepository.delete(organisme)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
