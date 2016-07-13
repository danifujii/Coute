package com.example.daniel.projectnutella.data;

/**
 * Created by Daniel on 13/7/2016.
 */
public class Pocket {
    private String name;
    private float balance;

    public Pocket(String n, float b){
        name = n;
        balance = b;
    }

    public float getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }
}
