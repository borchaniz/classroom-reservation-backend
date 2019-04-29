package com.sunshines.classrooms.Repository

import com.sunshines.classrooms.Model.*
import org.springframework.data .jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ReservationRepository : JpaRepository<Reservation, Int> {
    fun findByUser(user:User):List<Reservation>
}
