package com.example.backend.todo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "TODOS")
public class Todo {

  @Id
  @GeneratedValue
  private Long id;

  private Boolean isCompleted = false;
  private String description;
  private String title;

  protected Todo() {
  }

  public Todo(String description, String title) {
    this.description = description;
    this.title = title;
  }
}
