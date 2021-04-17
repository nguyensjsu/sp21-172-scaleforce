package com.example.backend.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@SpringBootTest
public class TodoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  /*
  following function sourced from https://howtodoinjava.com/spring-boot2/testing/spring-boot
   */
  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void getTodos_noTodos() throws Exception {
    mockMvc
        .perform(get("/todos"))
        .andExpect(status().isNotFound());
  }

  @Test
  void getTodo_noTodos() throws Exception {
    mockMvc
        .perform(get("/todos"))
        .andExpect(status().isNotFound());
  }

  @Test
  @DirtiesContext
  void postTodo_noBody() throws Exception {
    mockMvc
        .perform(
            post("/todos"))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DirtiesContext
  void postTodo() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath(("$.isCompleted")).exists())
        .andExpect(jsonPath("$.isCompleted").value(false))
        .andExpect(jsonPath(("$.description")).exists())
        .andExpect(jsonPath("$.description").value("sample description"))
        .andExpect(jsonPath(("$.title")).exists())
        .andExpect(jsonPath("$.title").value("sample title"));
  }

  @Test
  @DirtiesContext
  void postTodoGetTodos() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(get("/todos"))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$[0]")).exists())
        .andExpect(jsonPath(("$[0].isCompleted")).exists())
        .andExpect(jsonPath("$[0].isCompleted").value(false))
        .andExpect(jsonPath(("$[0].description")).exists())
        .andExpect(jsonPath("$[0].description").value("sample description"))
        .andExpect(jsonPath(("$[0].title")).exists())
        .andExpect(jsonPath("$[0].title").value("sample title"));
  }

  @Test
  @DirtiesContext
  void postTodoGetTodo() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(get("/todos/{id}", 1))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.isCompleted")).exists())
        .andExpect(jsonPath("$.isCompleted").value(false))
        .andExpect(jsonPath(("$.description")).exists())
        .andExpect(jsonPath("$.description").value("sample description"))
        .andExpect(jsonPath(("$.title")).exists())
        .andExpect(jsonPath("$.title").value("sample title"));
  }

  @Test
  @DirtiesContext
  void patchTodo_noBody() throws Exception {
    mockMvc
        .perform(patch("/todos/{id}", 1))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DirtiesContext
  void patchTodo_noTodos() throws Exception {
    Map<String, Object> requestBody = new HashMap<>();

    mockMvc
        .perform(patch("/todos/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestBody)))
        .andExpect(status().isNotFound());
  }

  @Test
  @DirtiesContext
  void patchTodo_UpdateIsCompleted() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("isCompleted", true);

    mockMvc
        .perform(patch("/todos/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestBody)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.isCompleted")).exists())
        .andExpect(jsonPath("$.isCompleted").value(true));
  }

  @Test
  @DirtiesContext
  void patchTodo_UpdateDescription() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("description", "new description");

    mockMvc
        .perform(patch("/todos/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestBody)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.description")).exists())
        .andExpect(jsonPath("$.description").value("new description"));
  }

  @Test
  @DirtiesContext
  void patchTodo_UpdateTitle() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("title", "new title");

    mockMvc
        .perform(patch("/todos/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(requestBody)))
        .andExpect(status().isOk())
        .andExpect(jsonPath(("$.title")).exists())
        .andExpect(jsonPath("$.title").value("new title"));
  }

  @Test
  @DirtiesContext
  void deleteTodo_noTodos() throws Exception {
    mockMvc
        .perform(delete("/todos/{id}", 1))
        .andExpect(status().isNotFound());
  }

  @Test
  @DirtiesContext
  void deleteTodo() throws Exception {
    Todo todo = new Todo("sample description", "sample title");

    mockMvc
        .perform(
            post("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(todo)))
        .andExpect(status().isCreated());

    mockMvc
        .perform(delete("/todos/{id}", 1))
        .andExpect(status().isOk());
  }
}
