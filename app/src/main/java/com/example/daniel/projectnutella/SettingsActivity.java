package com.example.daniel.projectnutella;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.MultiSelectListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

import com.example.daniel.projectnutella.data.CategoryManager;
import com.example.daniel.projectnutella.data.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
        private MultiSelectListPreference catsLP;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences_layout);

            setJumpPocketsLP();
            setCategoriesList();
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
            if (jumpLP.getValue() == null)
                jumpLP.setValueIndex(0);
        }

        private void setCategoriesList(){
            catsLP = (MultiSelectListPreference)findPreference(getString(R.string.pref_cats_enabled));
            DbHelper db = new DbHelper(getActivity());
            List<CharSequence> entriesList = new ArrayList<>();
            List<CharSequence> entryValuesList = new ArrayList<>();
            Cursor c = db.getCategories();
            int cat = 0;
            Set<String> values = new TreeSet<>();
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            while (c.moveToNext()){
                String name = c.getString(1);
                String key = WelcomeActivity.catPref + String.valueOf(cat);
                entriesList.add(name.charAt(0) + name.substring(1,name.length()).toLowerCase());
                entryValuesList.add(key);
                if (sp.getBoolean(key,true) && !name.equals(CategoryManager.cat_income))
                    values.add(key);
                cat++;
            }
            //Remove INCOME category
            entriesList.remove(entriesList.size()-1);
            entryValuesList.remove(entriesList.size()-1);

            //Populate the MultiSelectListPreference
            CharSequence[] entries = new CharSequence[c.getCount()-1];
            CharSequence[] entryValues = new CharSequence[c.getCount()-1];
            entriesList.toArray(entries);
            entryValuesList.toArray(entryValues);
            catsLP.setEntries(entries);
            catsLP.setEntryValues(entryValues);
            catsLP.setValues(values);
            catsLP.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sp.edit();
                    Set<String> values = (Set<String>)newValue;
                    Log.d("SETTINGS", values.toString());
                    for (int i = 0 ; i < catsLP.getEntries().length; i++)
                        editor.putBoolean(WelcomeActivity.catPref+String.valueOf(i),false);
                    for (String s: values)
                        editor.putBoolean(s, true);
                    editor.apply();
                    return true;
                }
            });
        }
    }
}
