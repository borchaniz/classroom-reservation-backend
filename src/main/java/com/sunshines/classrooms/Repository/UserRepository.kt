package com.sunshines.classrooms.Repository

import com.sunshines.classrooms.Model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {
    override fun findAll(): List<User>
    fun findAllByEmail(email: String): List<User>

    fun findFirstByEmail(email: String): User
    fun findFirstById(id: Int): User
}
