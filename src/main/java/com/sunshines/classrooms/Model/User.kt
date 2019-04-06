package com.sunshines.classrooms.Model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank


@Entity
@Table(name = "USERS")
data class User (

        @Id
        @GeneratedValue
        var id: Int = 0,
        @Column(name = "email")
        var email: String? = "",

        @NotBlank
        private var password: String? = null,

        @Transient
        private var cPassword: String? = null,

        @Column(name = "first_name")
        var first_name: String? = "",

        @Column(name = "last_name")
        var last_name: String? = "",

        @Column(name = "phone")
        var phone: String? = "",

        @Column(name = "cin")
        var cin: String? = "",

        @Column(name = "num_ins")
        var num_ins: String? = null,

        @Column(name = "status")
        var status: Int? = 0,

        @ManyToOne
        @JoinColumn(name = "role_id")
        var role: Role? = null
):UserDetails {
    val isValid: Boolean
        get() = !(this.password != this.cPassword || this.first_name == "" || this.last_name
                == "" || this.phone == "" || this.cin == "")

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(role!!.name))
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String? {
        return null
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    fun setPassword(password: String) {
        this.password = password
    }

    fun getcPassword(): String? {
        return cPassword
    }

    fun setcPassword(cPassword: String) {
        this.cPassword = cPassword
    }


}
