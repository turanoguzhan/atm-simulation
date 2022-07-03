package com.ouz.atmsimulation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authz) ->
                        authz.antMatchers("/swagger-ui/**","/console/**").permitAll()
                        .anyRequest().authenticated().and()
                )
                .httpBasic(withDefaults())
                .csrf().disable()
                .headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String securePass = passwordEncoder.encode("123456");

        UserDetails user = User.builder()
                .username("admin")
                .password("{bcrypt}"+securePass)
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}
