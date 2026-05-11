package com.anarkia.games.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer; // <-- Importante
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.anarkia.games.service.UsuarioService;

@Configuration
public class securityConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioService usuarioService, BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(usuarioService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http

            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth

                .requestMatchers(
                    "/", "/css/**", "/js/**", "/Imagenes/**", "/Fuente/**",
                    "/Tickets",
                    "/Merch", "/InicioSesion", "/Registrarse",
                    "/create-payment-intent-carrito",
                    "/Perfil/**",
                    "/pago-exitoso",
                    "/reset-password",
                    "/forgot-password",
                    "/stripe/events/**",
                    "/debug-security"
                ).permitAll()

                .requestMatchers("/AdminRegistro/**").hasAuthority("Administrador")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/InicioSesion")
                .permitAll()
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/InicioSesion?logout")
                .permitAll()
            );

        return http.build();
    }
}