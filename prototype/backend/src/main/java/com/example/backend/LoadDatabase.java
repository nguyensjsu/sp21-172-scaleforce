package com.example.backend;

import com.example.backend.todo.Todo;
import com.example.backend.todo.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LoadDatabase {

  @Bean
  CommandLineRunner initializeDatabase(TodoRepository todoRepository) {
    return args -> {
      todoRepository.save(new Todo(
          "Finish prototype",
          "Need to research deploying a React app to GKE"
      ));
    };
  }
}
