package com.example.daniel.projectnutella;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PocketActivity extends AppCompatActivity {

    private int pocketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pocket_fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("TITLE"));
        pocketId = extras.getInt("ID");

        List<Transaction> testList = new ArrayList<>();
        testList.add(new Transaction("500","10/6/2016",0,4,true));
        testList.add(new Transaction("30","20/4/2016",0,2,false));
        testList.add(new Transaction("15","21/5/2016",0,1,true));

        RecyclerView rv = (RecyclerView) findViewById(R.id.history_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new TransactionAdapter(testList,this));

        setBalance(testList);
    }

    public void setBalance(List<Transaction> transactions){
        double net = 0.0;
        for(Transaction t: transactions){
            if (t.getIsIncome())
                net = net + Double.valueOf(t.getAmount());
            else net = net - Double.valueOf(t.getAmount());
        }
        TextView balanceTV = (TextView)findViewById(R.id.amountTextView);
        balanceTV.setText("$"+net);
    }

}
