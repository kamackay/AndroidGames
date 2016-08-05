package com.keithmackay.games.androidgames.corners;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.keithmackay.games.androidgames.R;

import java.util.Timer;
import java.util.TimerTask;

public class CornersMain extends AppCompatActivity {

    private Timer waitTime;
    private TimerTask task;
    private int time;
    private int timeIncrement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_corners_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        waitTime = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                addTile();
            }
        };
        time = prefs.getInt(getString(R.string.settings_corners_startingTime), 5000);
        timeIncrement = prefs.getInt(getString(R.string.settings_corners_timeIncrement), 100);
        waitTime.schedule(task, time);
    }

    public void addTile() {
        Toast.makeText(getApplicationContext(), "Add A Tile", Toast.LENGTH_SHORT).show();
        time -= timeIncrement;
        waitTime.schedule(task, time);
    }
}
