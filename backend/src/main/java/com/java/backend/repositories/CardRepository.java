package com.java.backend.repositories;

import com.java.backend.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>
{

}
