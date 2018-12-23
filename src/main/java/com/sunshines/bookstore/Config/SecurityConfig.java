package com.sunshines.bookstore.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunshines.bookstore.Config.Filter.JWTAuthenticationFilter;
import com.sunshines.bookstore.Config.Filter.JWTAuthorizationFilter;
import com.sunshines.bookstore.Service.UserDetailsSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.sunshines.bookstore.Config.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebMvc
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsSvc userDetailsSvc;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers("/book/**").permitAll().and()
                .formLogin().successHandler(this::loginSuccessHandler)
//                authenticated()
                .and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilter(new JWTAuthorizationFilter(authenticationManager()));
    }

    private void loginSuccessHandler(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        response.addHeader("access-control-expose-headers", "Authorization");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsSvc).passwordEncoder(passwordEncoder);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        List<String> allowedOrigins = new ArrayList<>();
        allowedOrigins.add("*");
        configuration.setAllowedOrigins(allowedOrigins);
        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("HEAD");
        allowedMethods.add("GET");
        allowedMethods.add("POST");
        allowedMethods.add("PUT");
        allowedMethods.add("DELETE");
        configuration.setAllowedMethods(allowedMethods);
        configuration.setAllowCredentials(true);
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("Authorization");
        allowedHeaders.add("Cache-Control");
        allowedHeaders.add("Content-Type");
        configuration.setAllowedHeaders(allowedHeaders);
        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("Authorization");
        exposedHeaders.add("pragma");
        exposedHeaders.add("cache-control");
        exposedHeaders.add("expires");
        configuration.setExposedHeaders(exposedHeaders);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}