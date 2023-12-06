//package com.ukma.cleaning_v2.utils.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//
//@Configuration
//public class SecurityDetailsConfig {
//    @Bean
//    public InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password("$2a$12$U/z2AtiXAQg6YGhgSqu1lOET6w4CzHiLueUCw8WaMbHBpbPhRTg/e")
//                .roles("Admin")
//                .build();
//        UserDetails user = User.builder()
//                .username("user")
//                .password("$2a$12$auHFDxrqFESShy7Dlm8aSu/aSHFF/Wl64GQ0rgp/jf7YKfYC29GAi")
//                .roles("User")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
//}
