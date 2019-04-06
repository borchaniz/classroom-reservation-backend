package com.sunshines.classrooms.Repository

import com.sunshines.classrooms.Model.*
import org.springframework.data .jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation, Int> {
//    override fun findAll(): List<Type_Salle>
//    override fun findById(p0: Int): Optional<Type_Salle>
}
