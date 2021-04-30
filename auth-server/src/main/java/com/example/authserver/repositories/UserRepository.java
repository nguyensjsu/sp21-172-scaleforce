package com.example.authserver.repositories;

import com.example.authserver.entities.HaircutUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<HaircutUser, Long>
{
    HaircutUser findByEmail(String email);

}
