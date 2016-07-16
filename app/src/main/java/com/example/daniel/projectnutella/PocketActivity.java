package com.example.daniel.projectnutella;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.daniel.projectnutella.adapter.PocketAdapter;
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
    private RecyclerView rv;

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

        DbHelper db = new DbHelper(this);
        Cursor cursor = db.getTransactions(pocketId);
        List<Transaction> transactions = new ArrayList<>();
        while (cursor.moveToNext()){
            transactions.add(new Transaction(cursor.getString(1),cursor.getString(3),
                    cursor.getInt(4),cursor.getInt(5),cursor.getInt(2)>0));
        }

        rv = (RecyclerView) findViewById(R.id.history_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new TransactionAdapter(transactions,this));

        setBalance(transactions);

        ImageButton catsDetailButton = (ImageButton)findViewById(R.id.cats_detail_button);
        if (catsDetailButton != null)
            catsDetailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 16) {
                        Intent i = new Intent(PocketActivity.this, CatsGraphActivity.class);
                        i.putExtra("ID", pocketId);
                        PendingIntent pendingIntent =
                                TaskStackBuilder.create(PocketActivity.this)
                                        .addNextIntentWithParentStack(getIntent())
                                        .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(PocketActivity.this);
                        builder.setContentIntent(pendingIntent);
                        startActivity(i);
                    }
                }
            });
    }

    public void setBalance(List<Transaction> transactions){
        double net = 0.0;
        for(Transaction t: transactions){
            if (t.getIsIncome())
                net = net + Double.valueOf(t.getAmount());
            else net = net - Double.valueOf(t.getAmount());
        }
        TextView balanceTV = (TextView)findViewById(R.id.amountTextView);
        if (balanceTV != null)
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
                        Transaction newT = new Transaction(((EditText) dialogAddTrans.findViewById(R.id.amountET)).getText().toString()
                                ,fecha,pocketId,cat+1,isIncome());
                        db.insertTransaction(newT);
                        updateRVAdapter();
                    }
                    else {
                        Toast toast = Toast.makeText(PocketActivity.this,
                                getString(R.string.error_add_transaction), Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                //Do nothing when cancelling
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

    private void updateRVAdapter()
    {
        List transactions = new ArrayList<>();
        DbHelper db = new DbHelper(this);
        Cursor cursor = db.getTransactions(pocketId);
        while (cursor.moveToNext()){
            transactions.add(new Transaction(cursor.getString(1),cursor.getString(3),
                    cursor.getInt(4),cursor.getInt(5),cursor.getInt(2)>0));
        }
        cursor.close();
        rv.setAdapter(new TransactionAdapter(transactions,this));

        setBalance(transactions);
    }

    public void incomeClick(View v){
        if (!v.isSelected()) {   //By default buttons are not selected. To us, then this means its a used category
            v.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            v.setSelected(true);
        }
    }
}

