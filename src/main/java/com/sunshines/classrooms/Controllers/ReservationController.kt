package com.sunshines.classrooms.Controllers

import com.sunshines.classrooms.Exception.InvalidRequestException
import com.sunshines.classrooms.Exception.NotFoundException
import com.sunshines.classrooms.Model.*
import com.sunshines.classrooms.Repository.*
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
@RequestMapping("/reservation")
class ReservationController {

    @Autowired
    lateinit var reservationRepository: ReservationRepository

    @Autowired
    lateinit var salleRepository: SalleRepository

    @Autowired
    lateinit var userRepository: UserRepository

    @GetMapping("")
    fun getAll(): List<Reservation> =
            reservationRepository.findAll()

    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyAuthority('ADMIN','STUDENT','TEACHER')")
    fun getById(@PathVariable(value = "id") reservation_id: Int): ResponseEntity<Reservation> {
        return reservationRepository.findById(reservation_id).map { reservation ->
            ResponseEntity.ok(reservation)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping("")
    fun create(@Valid @RequestBody reservation: Reservation): Reservation {
        reservation.salle = salleRepository.findById(reservation.salle_id!!).orElse(null)

        if(reservation.salle == null)
            throw NotFoundException("salle not found")

        reservation.user = userRepository.findById(reservation.user_id!!).orElse(null)

        if(reservation.user == null)
            throw NotFoundException("user not found")


        return reservationRepository.save(reservation)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable(value = "id") reservation_id: Int,
                          @Valid @RequestBody newReservation: Reservation): ResponseEntity<Reservation> {
        if(newReservation.salle_id != null) {
            newReservation.salle = salleRepository.findById(newReservation.salle_id!!).orElse(null)

            if (newReservation.salle == null)
                throw NotFoundException("salle not found")
        }
        newReservation.status = 0
        return reservationRepository.findById(reservation_id).map { existingReservation->
            val updatedReservation: Reservation = existingReservation
                    .copy(start_date = newReservation.start_date,end_date= newReservation.end_date
                            ,description= newReservation.description,other_needs = newReservation.other_needs,
                            status = newReservation.status,user = newReservation.user, salle = newReservation.salle)
            ResponseEntity.ok().body(reservationRepository.save(updatedReservation))
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/accepter/{id}")
    fun accepter(@PathVariable(value = "id") reservation_id: Int): ResponseEntity<Reservation> {
        return reservationRepository.findById(reservation_id).map { existingReservation->
            val updatedReservation: Reservation = existingReservation
                    .copy(status = 1)
            ResponseEntity.ok().body(reservationRepository.save(updatedReservation))
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/refuser/{id}")
    fun refuser(@PathVariable(value = "id") reservation_id: Int): ResponseEntity<Reservation> {
        return reservationRepository.findById(reservation_id).map { existingReservation->
            val updatedReservation: Reservation = existingReservation
                    .copy(status = 2)
            ResponseEntity.ok().body(reservationRepository.save(updatedReservation))
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/encours/{id}")
    fun encours(@PathVariable(value = "id") reservation_id: Int): ResponseEntity<Reservation> {
        return reservationRepository.findById(reservation_id).map { existingReservation->
            val updatedReservation: Reservation = existingReservation
                    .copy(status = 3)
            ResponseEntity.ok().body(reservationRepository.save(updatedReservation))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") reservation_id: Int): ResponseEntity<Void> {

        return reservationRepository.findById(reservation_id).map { reservation  ->
            reservationRepository.delete(reservation)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}
