package com.example.daniel.projectnutella;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.daniel.projectnutella.data.CategoryManager;

public class WelcomeActivity extends AppCompatActivity {

    public static final String catPref = "cat_";
    public static final String loguedPref = "logued";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Si ya eligio las categorias, saltar esta pantalla
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
        if (sp.getBoolean(loguedPref,false)) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button nextButton = (Button)findViewById(R.id.next_button);
        if (nextButton != null)
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save preferences
                GridLayout gl = (GridLayout) findViewById(R.id.cat_grid_layout);
                if (gl != null) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WelcomeActivity.this);
                    SharedPreferences.Editor editor = sp.edit();
                    for (int i = 0; i < gl.getChildCount(); i++) {
                        ImageButton ib = (ImageButton) gl.getChildAt(i);
                        if (ib != null)
                            editor.putBoolean(catPref + String.valueOf(i), !ib.isSelected());
                    }
                    editor.putBoolean(loguedPref,true);
                    editor.apply();
                }

                //Go to MainActivity
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
            }
        });
    }

    public void catClick(View v){
        if (!v.isSelected()) {   //By default buttons are not selected. To us, then this means its a used category
            v.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.colorAccent), PorterDuff.Mode.MULTIPLY);
            v.setSelected(true);
        }
        else{
            v.getBackground().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            v.setSelected(false);
        }
    }
}
