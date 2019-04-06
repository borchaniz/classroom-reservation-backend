package com.sunshines.classrooms.Repository

import com.sunshines.classrooms.Model.Organisme
import com.sunshines.classrooms.Model.Role
import com.sunshines.classrooms.Model.Type_Organisme
import com.sunshines.classrooms.Model.Type_Salle
import org.springframework.data .jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TypeOrganismeRepository : JpaRepository<Type_Organisme, Int> {
//    override fun findAll(): List<Type_Salle>
//    override fun findById(p0: Int): Optional<Type_Salle>
}
