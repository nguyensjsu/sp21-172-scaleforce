package com.example.authserver.entities;

public enum Permission
{
    USER(1),
    OFFICE(500),
    ADMIN(1000);

    private final int rank;

    Permission(int rank)
    {
        this.rank = rank;
    }

    public int getRank()
    {
        return rank;
    }

    public boolean hasPermission(Permission otherPermission)
    {
        return rank >= otherPermission.rank;
    }
}
