package com.example.backend.todo;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TodoController {

  private final TodoRepository todoRepository;

  TodoController(TodoRepository todoRepository) {
    this.todoRepository = todoRepository;
  }

  @GetMapping("/todos")
  ResponseEntity<List<Todo>> getTodos() {
    List<Todo> todos = todoRepository.findAll();

    if (todos.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(todos, HttpStatus.OK);
  }

  @GetMapping("/todos/{id}")
  ResponseEntity<Optional<Todo>> getTodo(@PathVariable long id) {
    Optional<Todo> todo = todoRepository.findById(id);

    if (todo.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<>(todo, HttpStatus.OK);
  }

  @PostMapping("/todos")
  ResponseEntity<Todo> postTodo(@RequestBody Todo todo) {
    return new ResponseEntity<>(todoRepository.save(todo), HttpStatus.CREATED);
  }

  @PatchMapping("/todos/{id}")
  ResponseEntity<Todo> patchTodo(@PathVariable long id,
      @RequestBody Map<String, Object> requestBody) {
    Optional<Todo> todo = todoRepository.findById(id);

    if (todo.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Todo actualTodo = todo.get();

    if (requestBody.containsKey("isCompleted")) {
      actualTodo.setIsCompleted((boolean) requestBody.get("isCompleted"));
    }

    if (requestBody.containsKey("description")) {
      actualTodo.setDescription((String) requestBody.get("description"));
    }

    if (requestBody.containsKey("title")) {
      actualTodo.setTitle((String) requestBody.get("title"));
    }

    return new ResponseEntity<>(todoRepository.save(actualTodo), HttpStatus.OK);
  }

  @DeleteMapping("/todos/{id}")
  ResponseEntity<Void> deleteTodo(@PathVariable long id) {
    Optional<Todo> todo = todoRepository.findById(id);

    if (todo.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    Todo actualTodo = todo.get();
    todoRepository.delete(actualTodo);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
