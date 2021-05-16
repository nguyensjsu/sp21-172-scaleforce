package com.example.authserver.config;

import com.example.authserver.entities.HaircutUser;
import com.example.authserver.entities.Role;
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
            log.info("Preloading " + repository.save(new HaircutUser("Nick@email.test", "N", Role.ROLE_ADMIN)));
            log.info("Preloading " + repository.save(new HaircutUser("G@email.test", "G", Role.ROLE_OFFICE)));
            log.info("Preloading " + repository.save(new HaircutUser("Jake@email.test", "J", Role.ROLE_USER)));
        };
    }
}
