package com.example.daniel.projectnutella.data;

/**
 * Created by Daniel on 12/7/2016.
 */
public class Transaction {
    protected String amount;
    protected String date;
    protected int pocket;
    protected int cat;
    protected boolean isIncome;

    public Transaction(String amount, String date, int pocket, int cat, boolean isIncome){
        this.amount = amount;
        this.date = date;
        this.pocket = pocket;
        this.cat = cat;
        this.isIncome = isIncome;
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
}
