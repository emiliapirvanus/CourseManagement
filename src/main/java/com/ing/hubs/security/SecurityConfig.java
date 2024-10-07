package com.ing.hubs.security;

import com.ing.hubs.entity.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorize ->
                        authorize
                                //User Controller
                                .requestMatchers("/users/sessions").permitAll()
                                .requestMatchers("/users/**").hasAnyRole(UserRole.STUDENT.name(), UserRole.TEACHER.name())


                                //Student Controller
                                .requestMatchers(HttpMethod.POST, "/students").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/students").hasAnyRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.GET, "/students/**").hasAnyRole(UserRole.STUDENT.name(), UserRole.TEACHER.name())


                                //Teacher Controller
                                .requestMatchers(HttpMethod.POST, "/teachers").permitAll()
                                .requestMatchers("/teachers/**").hasAnyRole(UserRole.TEACHER.name())


                                //Schedule Controller
                                .requestMatchers("/schedules/**").hasAnyRole(UserRole.TEACHER.name())


                                //Enrollment Controller
                                .requestMatchers("/enrollments/me").hasAnyRole(UserRole.STUDENT.name())
                                .requestMatchers(HttpMethod.POST, "/enrollments").hasAnyRole(UserRole.STUDENT.name())

                                .requestMatchers(HttpMethod.GET, "/enrollments").hasAnyRole(UserRole.TEACHER.name())
                                .requestMatchers(HttpMethod.DELETE, "/enrollments").hasAnyRole(UserRole.TEACHER.name(), UserRole.STUDENT.name())


                                //Course Controller
                                .requestMatchers(HttpMethod.GET, "/courses/**").hasAnyRole(UserRole.STUDENT.name())
                                .requestMatchers( "/courses/**").hasAnyRole(UserRole.TEACHER.name())

                                .anyRequest().authenticated()
                )
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
