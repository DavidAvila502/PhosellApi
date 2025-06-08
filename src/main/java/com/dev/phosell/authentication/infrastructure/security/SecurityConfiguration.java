package com.dev.phosell.authentication.infrastructure.security;

import com.dev.phosell.user.domain.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        //Doc routes
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**")
                        .permitAll()

                        // auth route
                        .requestMatchers(HttpMethod.POST,"/api/v1/auth/login", "/api/v1/auth/register")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/refresh","/api/v1/auth/logout")
                        .authenticated()

                        //packages route
                        .requestMatchers(HttpMethod.GET,"/api/v1/packages")
                        .permitAll()

                        //client route
                        .requestMatchers( "/api/v1/clients/**").hasRole(Role.CLIENT.toString())

                        //photographer route
                        .requestMatchers( HttpMethod.GET,"/api/v1/photographers/me").hasRole(Role.PHOTOGRAPHER.toString())
                        .requestMatchers(HttpMethod.GET,"/api/v1/photographers/available").hasRole(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/v1/photographers").hasRole(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/photographers/{id}/in-service").hasAnyRole(Role.ADMIN.toString(),Role.PHOTOGRAPHER.toString())

                        // session route
                        //.requestMatchers("/api/v1/sessions/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/sessions").hasRole(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.GET,"/api/v1/sessions/available-slots").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/sessions").hasRole(Role.CLIENT.toString())
                        .requestMatchers(HttpMethod.PUT,"/api/v1/sessions/{id}/status")
                        .hasAnyRole(Role.CLIENT.toString(),Role.PHOTOGRAPHER.toString(),Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.PATCH,"/api/v1/sessions/{id}/cancel")
                        .hasAnyRole(Role.ADMIN.toString(),Role.CLIENT.toString())
                        .requestMatchers(HttpMethod.PATCH,"/api/sessions/{id}/complete")
                        .hasAnyRole(Role.ADMIN.toString(),Role.PHOTOGRAPHER.toString())
                        .requestMatchers(HttpMethod.GET,"/api/v1/sessions/photographer/me").hasRole(Role.PHOTOGRAPHER.toString())
                        .requestMatchers(HttpMethod.GET,"/api/v1/sessions/{id}")
                        .hasAnyRole(Role.ADMIN.toString(),Role.CLIENT.toString(),Role.PHOTOGRAPHER.toString())
                        .requestMatchers(HttpMethod.GET,"/api/v1/sessions/user/{id}").hasRole(Role.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST,"/api/v1/sessions/registrations").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/v1/sessions/swap-photographers").hasRole(Role.ADMIN.toString())

                        // all other routes
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8005"));
        configuration.setAllowedMethods(List.of("GET","POST"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}