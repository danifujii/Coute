package com.example.daniel.projectnutella;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daniel.projectnutella.adapter.PocketAdapter;
import com.example.daniel.projectnutella.adapter.TransactionAdapter;
import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;
import com.example.daniel.projectnutella.data.Pocket;

import java.util.List;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String prefsName = "NutellaPreferences";
    private TextView pocketDialogTV;
    private RecyclerView rv;
    private List<Pocket> pockets;
    private int swipedCard = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String keyJump = sp.getString(getString(R.string.pref_jump_pockets),
                "error");

        if (!keyJump.equals(getString(R.string.pref_no_jump)) && !keyJump.equals("error")){
            DbHelper db = DbHelper.getInstance(MainActivity.this);
            Intent i = new Intent(this, PocketActivity.class);
            i.putExtra("ID",Integer.valueOf(keyJump));
            i.putExtra("TITLE",db.getPocketName(Integer.valueOf(keyJump)));
            startActivity(i);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.title_all_pockets));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.main_fab);
        if (fab!=null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addPocket();
                }
            });
        }

        CategoryManager.insertCategoriesIntoDB(this);
        setRecyclerView();
        ImageButton settingsButton = (ImageButton)findViewById(R.id.settings_main_button);
        if (settingsButton != null)
            settingsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { startActivity(new Intent(MainActivity.this,SettingsActivity.class)); }
            });
    }

     private void addPocket(){
         AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
         builder.setTitle(getString(R.string.add_pocket_title));
         builder.setPositiveButton(R.string.add_button, new DialogInterface.OnClickListener() {
             public void onClick(DialogInterface dialog, int id) {
                 if (pocketDialogTV != null){
                     String pocket = pocketDialogTV.getText().toString();
                     if (!pocket.isEmpty()) {
                         DbHelper db = DbHelper.getInstance(MainActivity.this);
                         if (!db.existsPocket(pocket)) {
                             db.insertPocket(pocket);
                             updateRVAdapter();
                         } else {
                             Toast toast = Toast.makeText(MainActivity.this,
                                     getString(R.string.error_add_pocket), Toast.LENGTH_SHORT);
                             toast.show();
                         }
                     }
                 }
             }
         });
         builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
             public void onClick(DialogInterface dialog, int id){
                 //Do nothing when cancelling
             }
         });
         builder.setView(R.layout.add_pocket_dialog);
         AlertDialog alertDialog = builder.create();
         alertDialog.show();
         alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
         pocketDialogTV = (TextView) alertDialog.findViewById(R.id.pocket_name_text_view);
     }

    /**
     * Actualizar informacion dentro del RecyclerView,
     * obteniendo los pockets en el Cursor y pasandolos a lista (sol. temporal)
     */
    public void updateRVAdapter(){
        /**
         * HACER UN PROPER CURSOR ADAPTER
         * ASAP
         */
        pockets = new ArrayList<>();
        DbHelper db = DbHelper.getInstance(MainActivity.this);
        Cursor cursor = db.getPockets();
        while (cursor.moveToNext()){
            pockets.add(new Pocket(Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),Float.valueOf(cursor.getString(2))));
        }
        cursor.close();
        rv.setAdapter(new PocketAdapter(pockets,this));
    }

    public void setRecyclerView(){
        rv = (RecyclerView) findViewById(R.id.pockets_recycler_view);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        updateRVAdapter();
    }

    public void updatePocket(int pocketPos, String newName){
        DbHelper db = DbHelper.getInstance(MainActivity.this);
        Pocket p = pockets.get(pocketPos);
        db.updatePocket(p.getId(),newName);
        updateRVAdapter();
    }

    public void deletePocket(int pocketPos){
        DbHelper db = DbHelper.getInstance(MainActivity.this);
        Pocket p = pockets.get(pocketPos);
        db.deletePocket(p.getId());
        updateRVAdapter();
    }

    public Pocket getPocket(int pos){
        return pockets.get(pos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateRVAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
