package com.novatronic.masivas.backoffice.security.config;

import com.novatronic.masivas.backoffice.handler.CustomAccessDeniedHandler;
import com.novatronic.masivas.backoffice.handler.CustomAuthenticationEntryPoint;
import com.novatronic.masivas.backoffice.security.filter.CustomCorsFilter;
import com.novatronic.masivas.backoffice.security.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author Obi Consulting
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomCorsFilter corsFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomCorsFilter corsFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.corsFilter = corsFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
            CustomAuthenticationEntryPoint authEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        new AntPathRequestMatcher("/seguridad/acceso"),
                        new AntPathRequestMatcher("/util/**"),
                        new AntPathRequestMatcher("/swagger-resources/**"),
                        new AntPathRequestMatcher("/v3/api-docs/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/swagger-ui.html"),
                        new AntPathRequestMatcher("/webjars/**"),
                        new AntPathRequestMatcher("/configuration/ui"),
                        new AntPathRequestMatcher("/configuration/security")
                ).permitAll()
                .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                .authenticationEntryPoint(authEntryPoint) // ← Personaliza 401
                .accessDeniedHandler(accessDeniedHandler) // ← Personaliza 403
                )
                .addFilterBefore(this.corsFilter, CorsFilter.class)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
