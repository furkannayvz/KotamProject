package com.i2i.intern.kotam.aom.configuration;

import com.i2i.intern.kotam.aom.helper.CustomPasswordEncoder;
import com.i2i.intern.kotam.aom.repository.CustomerRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfig {

    private final CustomerRepository customerRepository;
    private final CustomPasswordEncoder customPasswordEncoder;

    public ApplicationConfig(CustomerRepository customerRepository,
                             CustomPasswordEncoder customPasswordEncoder) {
        this.customerRepository = customerRepository;
        this.customPasswordEncoder = customPasswordEncoder;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return msisdn -> customerRepository.findByMsisdn(msisdn)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with msisdn: " + msisdn));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public org.springframework.security.crypto.password.PasswordEncoder passwordEncoder() {
        return customPasswordEncoder; // Küçük harfle - field'ı döndürün
    }
}