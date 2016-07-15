package com.example.daniel.projectnutella;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;
import com.example.daniel.projectnutella.data.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PocketActivity extends AppCompatActivity {

    private int pocketId;
    private AlertDialog dialogAddTrans;

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
                    addTransaction();
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

    public void addTransaction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PocketActivity.this);
        builder.setTitle("Add Transaction");
        builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface d, int id) {
                if (dialogAddTrans != null) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String fecha=dateFormat.format(date);
                    DbHelper db = new DbHelper(PocketActivity.this);
                    int nroCat = ((LinearLayout) dialogAddTrans.findViewById(R.id.layoutAmounts)).getChildCount();
                    int cat = catSelected(nroCat);
                    if ( cat != -1 && !(((EditText) dialogAddTrans.findViewById(R.id.amountET)).getText().toString().isEmpty())){
                        Transaction newT = new Transaction(((EditText) dialogAddTrans.findViewById(R.id.amountET)).getText().toString(),fecha,pocketId,cat,isIncome());
                        db.insertTransaction(newT);
                    }
                    else {
                        Toast toast = Toast.makeText(PocketActivity.this,
                                getString(R.string.error_add_transaction), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });
        builder.setView(R.layout.add_transaction);
        dialogAddTrans = builder.create();
        dialogAddTrans.show();
    }

    public int catSelected(int childCount){
        for (int i = 0; i < childCount - 1; i++){
            if (dialogAddTrans != null)
                if (!((LinearLayout) dialogAddTrans.findViewById(R.id.layoutAmounts)).getChildAt(i).isSelected())
                    return i;
        }
        return -1;
    }

    public boolean isIncome() {
        if (dialogAddTrans != null)
            if (dialogAddTrans.findViewById(R.id.spendBT).isSelected())
                return false;
            else
                return true;
        return false; //Retorno falso por defecto
    }

}

