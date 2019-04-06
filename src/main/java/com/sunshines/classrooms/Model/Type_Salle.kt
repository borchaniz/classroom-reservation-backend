package com.sunshines.classrooms.Model

import javax.persistence.*

@Entity
@Table(name = "TYPES_SALLES")

data class Type_Salle (

    @Id
    @GeneratedValue
    var id: Int = 0,

    @Column(name = "label")
    var label: String ?= ""

){

    val isValid: Boolean
        get() = !(this.label ==null)

}
