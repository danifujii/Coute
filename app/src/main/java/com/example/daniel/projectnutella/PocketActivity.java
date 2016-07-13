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

import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.Transaction;

import java.util.ArrayList;
import java.util.List;

public class PocketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pocket_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //CHANGE LATER WITH DB
        setTitle("Billetera");

        List<Transaction> testList = new ArrayList<>();
        testList.add(new Transaction("$500","10/6/2016",0,0,true));
        testList.add(new Transaction("$30","20/4/2016",0,0,true));
        testList.add(new Transaction("$15","21/5/2016",0,0,true));
        Log.d("Here",String.valueOf(testList.size()));

        RecyclerView rv = (RecyclerView) findViewById(R.id.history_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new TransactionAdapter(testList));
    }

}
