package com.sunshines.classrooms.Security

import com.sunshines.classrooms.Service.UserDetailsSvc
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.servlet.config.annotation.EnableWebMvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.util.ArrayList

@Configuration
@EnableWebMvc
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var userDetailsSvc: UserDetailsSvc

    @Autowired
    private val unauthorizedHandler: JwtAuthenticationEntryPoint? = null

    @Bean
    fun jwtAuthenticationFilter(): JWTAuthenticationFilter {
        return JWTAuthenticationFilter()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }


    @Throws(Exception::class)
    public override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder?) {
        authenticationManagerBuilder!!
                .userDetailsService<UserDetailsSvc>(userDetailsSvc)
                .passwordEncoder(passwordEncoder())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/users/sign-up").permitAll()
                .antMatchers(HttpMethod.POST, "/user/signin").permitAll()
                .anyRequest().permitAll()
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Throws(IOException::class)
    private fun loginSuccessHandler(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        response.addHeader("access-control-expose-headers", "Authorization")
    }


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        val allowedOrigins = ArrayList<String>()
        allowedOrigins.add("*")
        configuration.allowedOrigins = allowedOrigins
        val allowedMethods = ArrayList<String>()
        allowedMethods.add("HEAD")
        allowedMethods.add("GET")
        allowedMethods.add("POST")
        allowedMethods.add("PUT")
        allowedMethods.add("DELETE")
        configuration.allowedMethods = allowedMethods
        configuration.allowCredentials = true
        val allowedHeaders = ArrayList<String>()
        allowedHeaders.add("Authorization")
        allowedHeaders.add("Cache-Control")
        allowedHeaders.add("Content-Type")
        configuration.allowedHeaders = allowedHeaders
        val exposedHeaders = ArrayList<String>()
        exposedHeaders.add("Authorization")
        exposedHeaders.add("pragma")
        exposedHeaders.add("cache-control")
        exposedHeaders.add("expires")
        configuration.exposedHeaders = exposedHeaders
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }


}