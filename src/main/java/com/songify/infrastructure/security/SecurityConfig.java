package com.songify.infrastructure.security;

import com.songify.domain.security.jwt.JwtAuthenticationFilter;
import com.songify.domain.usercrud.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
class SecurityConfig {

    public static final String DEFAULT_USER_ROLE = "ROLE_USER";
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationSuccessHandler successHandler, CustomOidcUserService customOidcUserService) throws Exception {
        http.csrf(cc -> cc.disable());
        http.cors(cors -> cors.disable());
        http.formLogin(fl -> fl.disable());
        http.httpBasic(hbc -> hbc.disable());


        http.oauth2Login(oAuth2 -> oAuth2.successHandler(successHandler)
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(
                        customOidcUserService
                )));
        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    if (request.getHeader("Accept").contains("text/html")) {
                        response.sendRedirect("/login-page");
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                    }
                })
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden"))
        );
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .deleteCookies("AuthorizationToken")
                .clearAuthentication(true));
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                authorize -> authorize
                        // public actions
                        .requestMatchers(HttpMethod.GET, "/").authenticated()
                        .requestMatchers(HttpMethod.GET, "/login-page").permitAll()
                        .requestMatchers(HttpMethod.POST,"/login/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/users/register/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/logout/**").authenticated()
                        // immutable infrastructure actions
                        .requestMatchers("/swagger-ui/**").authenticated()
                        .requestMatchers("/swagger-resources/**").authenticated()
                        .requestMatchers("/v3/api-docs/**").authenticated()
                        .requestMatchers(HttpMethod.GET,"/token/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/userDetails/**").authenticated()
                        // immutable domain actions
                        .requestMatchers(HttpMethod.GET, "/songs/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/artists/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/albums/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/genres/**").authenticated()
                        // mutable actions
                        .requestMatchers(HttpMethod.PUT, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/genres/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/songs/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/artists/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/albums/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/genres/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }

//    public Customizer<CorsConfigurer<HttpSecurity>> corsConfigurerCustomizer() {
//        return c -> {
//            CorsConfigurationSource source = request -> {
//                CorsConfiguration config = new CorsConfiguration();
//                config.setAllowedOrigins(
//                        List.of("http://localhost:3000"));
//                config.setAllowedMethods(
//                        List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
//                config.setAllowedHeaders(List.of("*"));
//                return config;
//            };
//            c.configurationSource(source);
//        };
//    }

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
