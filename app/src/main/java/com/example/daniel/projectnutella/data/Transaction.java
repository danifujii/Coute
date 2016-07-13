package com.example.daniel.projectnutella.data;

/**
 * Created by Daniel on 12/7/2016.
 */
public class Transaction {
    protected String amount;
    protected String date;

    public Transaction(String amount, String date){
        this.amount = amount;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }
}
