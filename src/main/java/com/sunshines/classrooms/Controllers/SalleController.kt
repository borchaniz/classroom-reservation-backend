package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Model.*
import com.sunshines.classrooms.Repository.*
import com.sunshines.classrooms.Security.JWTTokenProvider
import javassist.NotFoundException
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
@RequestMapping("/salle")
class SalleController {

    @Autowired
    lateinit var salleRepository: SalleRepository
    @Autowired
    lateinit var typeSalleRepository: TypeSalleRepository


    @GetMapping("")
    fun getAll(): List<Salle> =
            salleRepository.findAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") salle_id: Int): ResponseEntity<Salle> {
        return salleRepository.findById(salle_id).map { salle ->
            ResponseEntity.ok(salle)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody salle: Salle): Salle {
            salle.type_salle = typeSalleRepository.findById(salle.type_salle_id!!).orElse(null)

            if(salle.type_salle == null)
                throw NotFoundException("type salle not found")
            return salleRepository.save(salle)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") organisation_id: Int,
                          @Valid @RequestBody newSalle: Salle): ResponseEntity<Salle> {
        if(newSalle.type_salle_id != null) {
            newSalle.type_salle = typeSalleRepository.findById(newSalle.type_salle_id!!).orElse(null)

            if (newSalle.type_salle == null)
                throw NotFoundException("type salle not found")
        }
        return salleRepository.findById(organisation_id).map { existingSalle ->
            val updatedSalle: Salle = existingSalle
                    .copy(number = newSalle.number,capacity=newSalle.capacity
                            ,has_projector=newSalle.has_projector,type_salle = newSalle.type_salle)
            ResponseEntity.ok().body(salleRepository.save(updatedSalle))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") organisation_id: Int): ResponseEntity<Void> {

        return salleRepository.findById(organisation_id).map { salle  ->
            salleRepository.delete(salle)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
