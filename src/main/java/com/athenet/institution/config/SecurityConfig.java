package com.athenet.institution.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.athenet.institution.exception.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class SecurityConfig {

    /**
     * Origenes permitidos via CORS (front admin, front publico, y sus
     * variantes en local mientras se desarrolla). Ver application.yml /
     * variable de entorno CORS_ALLOWED_ORIGINS.
     */
    @Value("${athenet.cors.allowed-origins}")
    private List<String> allowedOrigins;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    // Instituciones, deportes, equipos y sedes son administradas
                    // exclusivamente por el rol `admin` 
                    .requestMatchers(HttpMethod.POST, "/api/v1/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/v1/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                    .anyRequest().authenticated()
            )
        
            .exceptionHandling(exceptions -> exceptions
                    .accessDeniedHandler(accessDeniedHandler())
                    .authenticationEntryPoint(authenticationEntryPoint())
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

        return http.build();
    }

    /**
     * 403: el usuario está autenticado pero no tiene el rol requerido
     * (ej. un `director` intentando administrar instituciones).
     */
    private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, ex) -> {
            ApiError body = new ApiError(
                    LocalDateTime.now(),
                    HttpStatus.FORBIDDEN.value(),
                    "Forbidden",
                    "No tienes permisos para realizar esta acción."
            );
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            OBJECT_MAPPER.writeValue(response.getWriter(), body);
        };
    }

    /**
     * 401: no se envió token, o el token es inválido/expiró.
     */
    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, ex) -> {
            ApiError body = new ApiError(
                    LocalDateTime.now(),
                    HttpStatus.UNAUTHORIZED.value(),
                    "Unauthorized",
                    "Debes autenticarte para realizar esta acción."
            );
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            OBJECT_MAPPER.writeValue(response.getWriter(), body);
        };
    }

    /**
     * Entra ID incluye los App Roles asignados al usuario en el claim `roles`
     * (tanto en el ID Token como en el Access Token). Este converter toma los roles
     * y los transforma en authorities de Spring Security con prefijo ROLE_,
     * para poder usar hasRole("ADMIN") en las reglas de arriba.
     *
     * Ej: claim roles = ["admin"] -> authority "ROLE_ADMIN"
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles == null) {
                return List.of();
            }
            return roles.stream()
                    .<GrantedAuthority>map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .collect(Collectors.toList());
        });
        return converter;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(allowedOrigins);
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));
        cfg.setAllowCredentials(true);
        cfg.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }
}
