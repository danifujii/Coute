package com.example.daniel.projectnutella.data;

import android.util.Log;

/**
 * Created by Daniel on 12/7/2016.
 */
public class Transaction {
    protected int id;
    protected String amount;
    protected String date;
    protected int pocket;
    protected int cat;
    protected boolean isIncome;
    protected String descr;

    public Transaction(int id, String amount, String date, int pocket, int cat, boolean isIncome, String descr){
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.pocket = pocket;
        this.cat = cat;
        this.isIncome = isIncome;
        this.descr = descr;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public int getPocket() {
        return pocket;
    }

    public int getCat() {
        return cat;
    }

    public boolean getIsIncome() { return isIncome; }

    public String getDescr() {
        return descr;
    }
}
