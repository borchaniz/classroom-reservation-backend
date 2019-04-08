package com.sunshines.classrooms.Model

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "RESERVATIONS")
data class Reservation (

    @Id
    @GeneratedValue
    var id: Int = 0,

    @Column(name = "start_date")
    var start_date: Date ?= null,

    @Column(name = "end_date")
    var end_date: Date ?= null,


    @Column(name = "description",length=512)
    var description: String ?= null,

    @Column(name = "other_needs",length=512)
    var other_needs: String ?= null,

    @Column(name = "status")
    var status: Int ?= 0,


    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null,

    @Transient
    var user_id : Int?=null,

    @ManyToOne
    @JoinColumn(name = "salle_id")
    var salle: Salle? = null,

    @Transient
    var salle_id : Int ?= null,

    @ManyToOne
    @JoinColumn(name = "organisme_id")
    var organisme: Organisme? = null,

    @Transient
    var organisme_id : Int ?= null
){
    val isValid: Boolean
        get() = !(this.start_date ==null || this.end_date ==null
                || this.start_date!!.after(this.end_date))

}