package com.example.daniel.projectnutella;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.daniel.projectnutella.adapter.CategoryAdapter;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Transaction;
import com.example.daniel.projectnutella.graphic.ExpGraphicView;

import java.util.ArrayList;
import java.util.List;

public class ExpensesActivity extends AppCompatActivity {

    private int pocketId;
    private List<Transaction> catsList;
    private Spinner sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pocketId = getIntent().getExtras().getInt("ID");
        setTitle(getString(R.string.expenses));

        updateData(0);

        sp = (Spinner)findViewById(R.id.filter_spinner);
        if (sp != null){
            List<String> filters = new ArrayList<>();
            filters.add(getString(R.string.filter_all));
            filters.add(getString(R.string.filter_week));
            filters.add(getString(R.string.filter_month));
            filters.add(getString(R.string.filter_year));
            ArrayAdapter<String> filterAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, filters);
            sp.setAdapter(filterAdapter);

            sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateData(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

    private void updateData(int range){
        DbHelper db = new DbHelper(this);
        Cursor c = db.getTransGroupedCat(pocketId, DbHelper.RANGES[range]);
        catsList = new ArrayList<>();
        while (c.moveToNext()) {
            Log.d("UPDATE DATA",String.valueOf(c.getInt(0)));
            catsList.add(new Transaction(c.getString(0), "", 0, c.getInt(1), true));
        }
        catsList.add(new Transaction("60","",0,3,true));

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
        if (egv != null) {
            egv.setCategories(catsList, total, getScreenWidthDP());
            egv.invalidate();
        }
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
