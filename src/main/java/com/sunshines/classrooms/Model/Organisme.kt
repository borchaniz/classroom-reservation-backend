package com.sunshines.classrooms.Model

import java.util.*
import javax.persistence.*
import kotlin.jvm.Transient

@Entity
@Table(name = "ORGANISMES")
data class Organisme (

    @Id
    @GeneratedValue
    var id: Int = 0,

    @Column(name = "label")
    var label: String ?= "",


    @ManyToOne
    @JoinColumn(name = "type_id")
    var type: Type_Organisme? = null,

    @Transient
    var type_id : Int ? = null
){
    val isValid: Boolean
        get() = !(this.label ==null)

}