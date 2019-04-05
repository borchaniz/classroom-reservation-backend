package com.sunshines.classrooms.Model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank


@Entity
@Table(name = "users")
class User : UserDetails {

    @Id
    @GeneratedValue
    var id: Int = 0

    var email: String? = null

    @NotBlank
    private var password: String? = null

    @Transient
    private var cPassword: String? = null

    var name: String? = null

    @Column(name = "family_name")
    var familyName: String? = null

    var phone: Int = 0

    @ManyToOne
    @JoinColumn(name = "role_id")
    var role: Role? = null

    val isValid: Boolean
        get() = !(this.password != this.cPassword || this.name == "" || this.familyName == "")

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
