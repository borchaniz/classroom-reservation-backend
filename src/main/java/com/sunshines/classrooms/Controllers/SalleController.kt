package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.NotFoundException
import com.sunshines.classrooms.Model.Reservation
import com.sunshines.classrooms.Model.Salle
import com.sunshines.classrooms.Repository.ReservationRepository
import com.sunshines.classrooms.Repository.SalleRepository
import com.sunshines.classrooms.Repository.TypeSalleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/salle")
class SalleController {

    @Autowired
    lateinit var salleRepository: SalleRepository
    @Autowired
    lateinit var typeSalleRepository: TypeSalleRepository

    @Autowired
    lateinit var reservationRepository: ReservationRepository


    @GetMapping("")
    fun getAll(): List<Salle> {
        val salles = salleRepository.findAll()
        for (salle in salles) {
            val reservations = reservationRepository.findBySalle(salle).filter {
                it.status == 1 || it.status == 3
            }
            for (reservation in reservations) {
                salle.reservations.add(Reservation(reservation.id, reservation.start_date, reservation.end_date))
            }
        }
        return salles

    }

    @GetMapping("/{id}")
    fun getById(@PathVariable(value = "id") salle_id: Int): ResponseEntity<Salle> {
        return salleRepository.findById(salle_id).map { salle ->
            ResponseEntity.ok(salle)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody salle: Salle): Salle {
        salle.type = typeSalleRepository.findById(salle.type_salle_id!!).orElse(null)

        if (salle.type == null)
            throw NotFoundException("type salle not found")
        return salleRepository.save(salle)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") organisation_id: Int,
               @Valid @RequestBody newSalle: Salle): ResponseEntity<Salle> {
        if (newSalle.type_salle_id != null) {
            newSalle.type = typeSalleRepository.findById(newSalle.type_salle_id!!).orElse(null)

            if (newSalle.type == null)
                throw NotFoundException("type salle not found")
        }
        return salleRepository.findById(organisation_id).map { existingSalle ->
            val updatedSalle: Salle = existingSalle
                    .copy(number = newSalle.number, capacity = newSalle.capacity
                            , has_projector = newSalle.has_projector, type = newSalle.type)
            ResponseEntity.ok().body(salleRepository.save(updatedSalle))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") organisation_id: Int): ResponseEntity<Void> {

        return salleRepository.findById(organisation_id).map { salle ->
            salleRepository.delete(salle)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
