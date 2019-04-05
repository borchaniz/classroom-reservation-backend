package com.sunshines.classrooms.Repository

import com.sunshines.classrooms.Model.Role
import org.springframework.data .jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Int> {
    override fun findAll(): List<Role>
    fun findFirstByName(name: String): Role
}
