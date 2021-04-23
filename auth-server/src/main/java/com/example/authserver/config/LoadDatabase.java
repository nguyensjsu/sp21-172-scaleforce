package com.example.authserver.config;

import com.example.authserver.entities.Permission;
import com.example.authserver.entities.User;
import com.example.authserver.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new User("Nick", "N", Permission.ADMIN)));
            log.info("Preloading " + repository.save(new User("G", "G", Permission.OFFICE)));
            log.info("Preloading " + repository.save(new User("Jake", "J", Permission.USER)));
        };
    }
}
