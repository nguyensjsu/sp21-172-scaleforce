package com.example.authserver.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;


public class HaircutUserDetailContainer implements UserDetails
{
    private static final Logger logger = LoggerFactory.getLogger(HaircutUserDetailContainer.class);

    private final HaircutUser haircutUser;

    public HaircutUserDetailContainer(HaircutUser user)
    {
        this.haircutUser = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        for (Permission p : Permission.values())
        {
            if (haircutUser.getPermission().hasPermission(p))
                authorities.add(new SimpleGrantedAuthority(p.toString()));
        }
        logger.info(String.format("Authorized %s with permissions: %s", haircutUser.getEmail(), authorities));
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return haircutUser.getPassword();
    }

    @Override
    public String getUsername()
    {
        return haircutUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        // accounts don't expire
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        // accounts can't be locked
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        // passwords don't expire
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        // accounts can't be disabled
        return true;
    }
}
