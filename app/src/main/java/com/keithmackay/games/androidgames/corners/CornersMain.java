package com.keithmackay.games.androidgames.corners;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;

import com.keithmackay.games.androidgames.GameActivity;
import com.keithmackay.games.androidgames.R;

public class CornersMain extends GameActivity {
    private int time, timeIncrement;
    private final int minTime = 300;
    private boolean keepRunning = true;
    private CornersBoard board;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cornersmain);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        time = prefs.getInt(getString(R.string.settings_corners_startingTime), 5000);
        timeIncrement = prefs.getInt(getString(R.string.settings_corners_timeIncrement), 100);
        board = (CornersBoard) findViewById(R.id.corners_board);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (keepRunning) {
                    try {
                        Thread.sleep(time);
                        if (!keepRunning) return;
                        addTile();
                        if (time > minTime) time -= timeIncrement;
                        if (time < minTime) time = minTime;
                    } catch (final Exception e) {
                        toast(e.getMessage());
                        break;
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        keepRunning = false;
        super.onPause();
    }

    public void addTile() {
        //NOTE - this will mainly be run async
        try {
            //TODO: Actually add tile
            toast("Add A Tile");
        } catch (final Exception e) {
            toast(e.getMessage());
        }
    }


}
