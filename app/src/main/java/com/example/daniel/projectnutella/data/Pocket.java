package com.example.daniel.projectnutella.data;

/**
 * Created by Daniel on 13/7/2016.
 */
public class Pocket {
    private int id;
    private String name;
    private float balance;

    public Pocket(int id, String n, float b){
        this.id = id;
        name = n;
        balance = b;
    }

    public int getId() { return id; }

    public float getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }
}
