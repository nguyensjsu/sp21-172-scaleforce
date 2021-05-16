package com.java.backend.entities;

public enum HaircutService
{
    TRIM(3, 15),
    CUT_AND_BEARD(45,30),
    SHAVE(45,30);

    // cost in dollars
    private final int cost;
//    private final int length;
    HaircutService(int cost, int length)
    {
        this.cost = cost;
//        this.length = length;
    }

    public int getCost()
    {
        return cost;
    }

//    public int getLength()
//    {
//        return length;
//    }
}
