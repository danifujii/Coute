package com.example.daniel.projectnutella;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.example.daniel.projectnutella.data.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        toolbar.setTitle(getString(R.string.title_activity_settings));
        toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.colorWhite));


    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PrefsFragment.class.getName().equals(fragmentName);
    }

    public static class PrefsFragment extends PreferenceFragment {

        private ListPreference jumpLP;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_layout);

            setJumpPocketsLP();
        }

        private void setJumpPocketsLP(){
            DbHelper db = new DbHelper(getActivity());
            Cursor c = db.getPockets();
            List<CharSequence> entriesList = new ArrayList<>();
            List<CharSequence> entryValuesList= new ArrayList<>();
            while (c.moveToNext()) {
                entriesList.add(c.getString(1));
                entryValuesList.add(c.getString(0));
            }
            entriesList.add(0,getString(R.string.pref_no_jump));
            entryValuesList.add(0,getString(R.string.pref_no_jump));
            CharSequence[] entries = new CharSequence[c.getCount()+1];
            entriesList.toArray(entries);
            CharSequence[] entryValues = new CharSequence[c.getCount()+1];
            entryValuesList.toArray(entryValues);
            c.close();

            jumpLP = (ListPreference)findPreference(getString(R.string.pref_jump_pockets));
            jumpLP.setEntries(entries);

            jumpLP.setEntryValues(entryValues);
            jumpLP.setValueIndex(0);
        }
    }
}
