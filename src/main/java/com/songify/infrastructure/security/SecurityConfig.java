package com.songify.infrastructure.security;

import com.songify.domain.usercrud.UserRepository;
import com.songify.infrastructure.security.jwt.JwtAuthTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
class SecurityConfig {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";

    @Bean
    public UserDetailsManager userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws AuthenticationException {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthTokenFilter jwtAuthTokenFilter) throws Exception {
        http.csrf(cc -> cc.disable());
        http.cors(corsConfigurerCustomizer());
        http.formLogin(flc -> flc.disable());
        http.httpBasic(hbc -> hbc.disable());
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests(
                authorize -> authorize
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/users/register/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/songs/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/artists/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/albums/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/genres/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/token/**").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/songs/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/artists/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/albums/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/genres/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/songs/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/artists/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/albums/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/genres/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/songs/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/artists/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/albums/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/genres/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/songs/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/artists/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/albums/**" ).hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/genres/**" ).hasRole("ADMIN")
                                .anyRequest().authenticated());
        return http.build();
    }

    public Customizer<CorsConfigurer<HttpSecurity>> corsConfigurerCustomizer() {
        return c -> {
            CorsConfigurationSource source = request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(
                        List.of("http://localhost:3000"));
                config.setAllowedMethods(
                        List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            };
            c.configurationSource(source);
        };
    }

    //    @Bean
//    public UserDetailsService userDetailsService() {
//        var manager = new InMemoryUserDetailsManager();
//
//        var user1 = User.withUsername("User")
//                .password("12345")
//                .roles("USER")
//                .build();
//        var admin1 = User.withUsername("Admin")
//                .password("12345")
//                .roles("USER", "ADMIN")
//                .build();
//        manager.createUser(user1);
//        manager.createUser(admin1);
//        return manager;
//    }
}
