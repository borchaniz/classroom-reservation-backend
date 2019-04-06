package com.sunshines.classrooms.Model

import javax.persistence.*

@Entity
@Table(name = "TYPES_ORGANISMES")

data class Type_Organisme (

    @Id
    @GeneratedValue
    var id: Int = 0,

    @Column(name = "label")
    var label: String ?= ""
)
{
    val isValid: Boolean
        get() = !(this.label ==null)

}
