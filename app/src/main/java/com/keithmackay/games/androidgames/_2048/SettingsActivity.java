package com.keithmackay.games.androidgames._2048;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.keithmackay.games.androidgames.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048_settings);
        try {
            getActionBar().setTitle(getString(R.string.settings));
        } catch (Exception e) {
            Log.e("Error", "Could not update ActionBar Title", e);
        }
        Spinner spinSwipeLength = (Spinner) findViewById(R.id.spinner_swipeLength);
        if (spinSwipeLength != null) {
            try {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.swipeOptions, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinSwipeLength.setAdapter(adapter);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int len = prefs.getInt(getString(R.string.settings_swipeLen), 3);
                spinSwipeLength.setSelection(len);
                spinSwipeLength.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences.Editor prefEdit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        prefEdit.putInt(getString(R.string.settings_swipeLen), position);
                        prefEdit.apply();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        SharedPreferences.Editor prefEdit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                        prefEdit.putInt(getString(R.string.settings_swipeLen), 3);
                        prefEdit.apply();
                    }
                });
            } catch (Exception e) {
                Log.e("Error", "Error", e);
            }
        }
    }
}
