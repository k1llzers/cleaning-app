//package com.ukma.cleaning_v2.utils.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//
//import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
//
//@Configuration
//public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(
//                        config -> config
//                                //address controller
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/addresses")).hasAnyRole("User", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/addresses")).hasAnyRole("User", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.POST, "/addresses/**")).hasAnyRole("User", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/addresses/*")).hasAnyRole("User", "Employee", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/addresses/**")).hasAnyRole("User", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/addresses/by-user/**")).hasAnyRole("User","Admin")
//                                //commercial-proposals controller
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/commercial-proposals")).hasAnyRole("Admin")
//                                .requestMatchers(antMatcher(HttpMethod.POST, "/commercial-proposals")).hasAnyRole("Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/commercial-proposals/**")).permitAll()
//                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/commercial-proposals/**")).hasAnyRole("Admin")
//                                //user controller
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/users")).hasAnyRole("User", "Admin")
//                                .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("User", "Admin")
////                                .requestMatchers(HttpMethod.POST, "/users").hasAnyRole("User", "Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/users/**")).hasAnyRole("Admin")
//                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/users/**")).hasAnyRole("Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/users/by-email/**")).hasAnyRole("Admin")
//                                //comment controller
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/comments")).hasAnyRole("User")
//                                .requestMatchers(antMatcher(HttpMethod.POST, "/comments/**")).hasAnyRole("User")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/comments/**")).hasAnyRole("Admin")
//                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/comments/**")).hasAnyRole("Admin")
//                                //order controller
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/orders")).hasAnyRole("User","Admin")
//                                .requestMatchers(antMatcher(HttpMethod.POST, "/orders")).hasAnyRole("User","Admin")
//                                .requestMatchers(antMatcher(HttpMethod.PUT, "/orders/status")).hasAnyRole("User","Employee","Admin")
//                                .requestMatchers(antMatcher(HttpMethod.GET, "/orders/**")).hasAnyRole("User","Employee","Admin")
//                                .requestMatchers(antMatcher("/swagger-ui/**")
//                                        , antMatcher("/v2/api-docs")
//                                        , antMatcher("/swagger-resources")
//                                        , antMatcher("/swagger-resources/**")
//                                        , antMatcher("/configuration/ui")
//                                        , antMatcher("/configuration/security")
//                                        , antMatcher("/swagger-ui.html")
//                                        , antMatcher("/webjars/**")
//                                        , antMatcher("/v3/api-docs/**")).hasRole("Admin")
//                                .requestMatchers(antMatcher("/h2-console/**")).hasRole("Admin")
//                                .requestMatchers(antMatcher("/**")).permitAll()
//                )
//                .formLogin(login -> login.loginPage("/login").permitAll().failureUrl("/login?error"))
//                .headers(headers -> headers.frameOptions().disable())
//                .csrf(csrf -> csrf
//                        .ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")));
//        http.httpBasic(Customizer.withDefaults());
//        http.csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//}
