package com.example.authserver.entities;

public enum Role
{
    ROLE_USER(1),
    ROLE_OFFICE(500),
    ROLE_ADMIN(1000);

    private final int rank;

    Role(int rank)
    {
        this.rank = rank;
    }

    public int getRank()
    {
        return rank;
    }

    public boolean hasPermission(Role otherRole)
    {
        return rank >= otherRole.rank;
    }
}
