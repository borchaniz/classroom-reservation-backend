package com.sunshines.classrooms.Model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "SALLES")
data class Salle (

    @Id
    @GeneratedValue
    var id: Int = 0,

    @Column(name = "number")
    var number: String ?= "",

    @Column(name = "capacity")
    var capacity: Int ?= null,

    @Column(name = "has_projector")
    var has_projector: Int ?= 0,

    @ManyToOne
    @JoinColumn(name = "type_salle_id")
    var type_salle: Type_Salle? = null,

    @Transient
    var type_salle_id : Int ?= null
)

{
    val isValid: Boolean
        get() = !(this.number =="")

}