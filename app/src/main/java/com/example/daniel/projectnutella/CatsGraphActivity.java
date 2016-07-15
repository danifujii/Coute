package com.example.daniel.projectnutella;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.TextView;

import com.example.daniel.projectnutella.adapter.CategoryAdapter;
import com.example.daniel.projectnutella.data.Transaction;
import com.example.daniel.projectnutella.graphic.ExpGraphicView;

import java.util.ArrayList;
import java.util.List;

public class CatsGraphActivity extends AppCompatActivity {

    private int pocketId;
    private List<Transaction> catsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats_graph);

        pocketId = getIntent().getExtras().getInt("ID");
        setTitle(getString(R.string.expenses));

        catsList = new ArrayList<>();
        catsList.add(new Transaction("500","",0,4,true));
        catsList.add(new Transaction("30","",0,2,false));
        catsList.add(new Transaction("15","",0,1,true));

        RecyclerView rv = (RecyclerView) findViewById(R.id.cats_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new CategoryAdapter(this,catsList));

        setGraphic();
    }

    private void setGraphic(){
        double total = getGastosTotal();
        ExpGraphicView egv = (ExpGraphicView) findViewById(R.id.expenses_graphic_view);
        if (egv != null)
            egv.setCategories(catsList,total,getScreenWidthDP());
        TextView expensesTv = ((TextView)findViewById(R.id.expenses_text_view));
        if (expensesTv != null)
            expensesTv.setText("$"+String.valueOf(total));
    }

    private double getGastosTotal(){
        double sum = 0.0;
        for(Transaction t: catsList)
            sum = sum + Double.valueOf(t.getAmount());
        return sum;
    }

    private double getScreenWidthDP(){
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density  = getResources().getDisplayMetrics().density;
        //return outMetrics.widthPixels / density;
        return outMetrics.widthPixels;
    }
}
