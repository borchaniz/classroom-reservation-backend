package com.sunshines.classrooms.Model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name="ROLES")
class Role {
    @Id
    @GeneratedValue
    var id: Int = 0

    var name: String? = null
}
