package com.example.authserver.controllers;


import com.example.authserver.entities.HaircutUser;
import com.example.authserver.entities.HaircutUserDetailContainer;
import com.example.authserver.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService
{
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        HaircutUser haircutUser = userRepository.findByEmail(email);
        if (haircutUser == null)
        {
            throw new UsernameNotFoundException("Username not found");
        }
        return new HaircutUserDetailContainer(haircutUser);
    }
}
