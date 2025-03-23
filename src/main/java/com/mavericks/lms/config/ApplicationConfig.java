package com.mavericks.lms.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Application configuration class.
 * This class configures the beans needed for the application.
 */
@Configuration
@EnableJpaAuditing
public class ApplicationConfig {

    /**
     * Create a model mapper bean.
     * This bean is used to map between DTOs and entities.
     *
     * @return the model mapper bean
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        return modelMapper;
    }

    /**
     * Create a password encoder bean.
     * This bean is used to hash and verify passwords.
     *
     * @return the password encoder bean
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
