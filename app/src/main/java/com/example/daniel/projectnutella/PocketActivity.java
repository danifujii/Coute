package com.example.daniel.projectnutella;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PocketActivity extends AppCompatActivity {

    private int pocketId;
    private AlertDialog dialogAddTrans;
    private RecyclerView rv;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pocket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.pocket_fab);
        if (fab != null)
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addTransaction();
                }
            });

        //Get Intent data
        Bundle extras = getIntent().getExtras();
        setTitle(extras.getString("TITLE"));
        pocketId = extras.getInt("ID");

        db = new DbHelper(this);
        //Get all transactions for setting the balance
        setBalance();

        //Setup the ViewPager
        updateViewPager();

        //Setup Expenses graphic button
        ImageButton catsDetailButton = (ImageButton)findViewById(R.id.cats_detail_button);
        if (catsDetailButton != null)
            catsDetailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 16) {
                        Intent i = new Intent(PocketActivity.this, ExpensesActivity.class);
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

        ImageButton settingsButton = (ImageButton)findViewById(R.id.settings_pocket_button);
        if (settingsButton != null)
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(PocketActivity.this,SettingsActivity.class));
                }
            });
    }

    public void setBalance(){
        Cursor cursor = db.getTransactions(pocketId);
        double net = 0.0;
        while (cursor.moveToNext()){
            if (cursor.getInt(2)>0)
                net = net + Double.valueOf(cursor.getString(1));
            else net = net - Double.valueOf(cursor.getString(1));
        }
        cursor.close();
        TextView balanceTV = (TextView)findViewById(R.id.amountTextView);
        if (balanceTV != null) {
            if (net >= 0)
                balanceTV.setText("$" + net);
            else balanceTV.setText("-$" + Math.abs(net));
        }
    }

    public void addTransaction(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PocketActivity.this);
        builder.setTitle("Add Transaction");
        builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface d, int id) {
                if (dialogAddTrans != null) {
                    String fecha = getCurrentDate(true);
                    String amount = ((EditText) dialogAddTrans.findViewById(R.id.amount_edit_text)).getText().toString();
                    if (!amount.isEmpty()){
                        Transaction newT;
                        if (isIncome())
                            newT = new Transaction(0,amount
                                    ,fecha,pocketId,db.getCategoryId(CategoryManager.cat_income),true);
                        else{
                            int cat = catSelected();
                            newT = new Transaction(0,amount
                                    ,fecha,pocketId,cat+1,false);
                        }
                        db.insertTransaction(newT);
                        updateViewPager();
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
        ImageButton catButton = (ImageButton)((LinearLayout)dialogAddTrans.findViewById(R.id.add_trans_layout_cats)).getChildAt(0);
        if (catButton!=null) { changeButtonBG(catButton,true); }
        Button incomeButton = (Button)((LinearLayout)dialogAddTrans.findViewById(R.id.add_trans_layout_income)).getChildAt(0);
        if (incomeButton!=null) {
            changeButtonBG(incomeButton,true);
            incomeButton.setTextColor(ContextCompat.getColor(this,R.color.colorWhite));
        }
        //dialogAddTrans.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    //Get the category that was selected in the Add Transaction Dialog
    public int catSelected(){
        if (dialogAddTrans != null) {
            LinearLayout ll = (LinearLayout) dialogAddTrans.findViewById(R.id.add_trans_layout_cats);
            if (ll != null)
            for (int i = 0; i < ll.getChildCount(); i++) {
                if (ll.getChildAt(i).isSelected())
                    return i;
            }
        }
        return -1;
    }

    public void categoryClick(View v){
        if (!v.isSelected()){
            LinearLayout ll = (LinearLayout)v.getParent();
            for (int i = 0; i < ll.getChildCount(); i++){
                View button = ll.getChildAt(i);
                changeButtonBG(button,false);
            }
            changeButtonBG(v,true);
        }
    }

    public void incomeClick(View v)
    {
        if (!v.isSelected()) {
            categoryClick(v);
            LinearLayout ll = (LinearLayout) v.getParent();
            for (int i = 0; i < ll.getChildCount(); i++) {
                Button button = (Button) ll.getChildAt(i);
                button.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            }
            ((Button)v).setTextColor(ContextCompat.getColor(this, R.color.colorWhite));

            if (v.getId()==R.id.gain_button)
                dialogAddTrans.findViewById(R.id.add_trans_layout_cats).setVisibility(View.GONE);
            else dialogAddTrans.findViewById(R.id.add_trans_layout_cats).setVisibility(View.VISIBLE);
        }
    }

    public void changeButtonBG(View v, boolean active){
        if (active){
            v.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            v.setSelected(true);
        }
        else{
            v.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            v.setSelected(false);
        }
    }

    public boolean isIncome() {
        if (dialogAddTrans != null)
            return (dialogAddTrans.findViewById(R.id.gain_button).isSelected());
        return false; //Retorno falso por defecto
    }

    public void quickAddAmount(View v){
        String text = ((Button)v).getText().toString();
        double amount = Double.valueOf(text.substring(1,text.length()));    //remove the $ sign
        if (dialogAddTrans != null){
            TextView amountTV = (TextView)dialogAddTrans.findViewById(R.id.amount_edit_text);
            String currentAmount = amountTV.getText().toString();
            if (currentAmount.isEmpty())
                amountTV.setText(String.valueOf(amount));
            else{
                double current = Double.valueOf(currentAmount);
                amountTV.setText(String.valueOf(current + amount));
            }
        }
    }

    private void updateViewPager()
    {
        ViewPager mPager = (ViewPager)findViewById(R.id.day_pager);
        if (mPager != null) {
            DayTransPagerAdapter dpa = new DayTransPagerAdapter(getSupportFragmentManager());
            dpa.setDates(db.getDates(pocketId));
            mPager.setAdapter(dpa);
            mPager.setCurrentItem(dpa.getCount()-1);
        }
        setBalance();
    }

    public String getCurrentDate(boolean withTime){
        SimpleDateFormat dateFormat;
        if (withTime)
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        else dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private class DayTransPagerAdapter extends FragmentStatePagerAdapter{
        private Cursor dates;

        public DayTransPagerAdapter(FragmentManager fm){ super (fm); }

        public void setDates(Cursor c) { dates = c; }

        @Override
        public Fragment getItem(int position) {
            TransFragment tf = new TransFragment();
            if (dates.moveToPosition(position)) {
                String date = dates.getString(0);
                DbHelper db = new DbHelper(PocketActivity.this);
                Cursor c = db.getTransactions(pocketId,date);
                List<Transaction> transactions = new ArrayList<>();
                while (c.moveToNext()) {
                    transactions.add(new Transaction(c.getInt(0)
                            ,String.valueOf(c.getDouble(1)), c.getString(3),
                            c.getInt(4), c.getInt(5), c.getInt(2) > 0));
                }
                tf.setDate(date,transactions);
                c.close();
            }
            else
                tf.setDate(getCurrentDate(false),new ArrayList<Transaction>());
            return tf;
        }

        @Override
        public int getCount() {
            if (dates.getCount() > 0)
                return dates.getCount();
            else return 1;
        }
    }
}

