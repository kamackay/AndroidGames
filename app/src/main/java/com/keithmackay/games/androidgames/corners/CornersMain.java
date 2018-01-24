package com.keithmackay.games.androidgames.corners;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.common.GameActivity;
import com.keithmackay.games.androidgames.common.GameEventHandler;
import com.keithmackay.games.androidgames.common.GameTimer;

import java.util.Locale;
import java.util.Random;

public class CornersMain extends GameActivity {
    private static final int minTime = 600;
    private int time, timeIncrement, score, moves, maxValLastIncr;
    private boolean keepRunning = true;
    private CornersBoard board;
    private TextView scoreView;
    private GameTimer timerView;
    private boolean gameLost;
    private StressLevel stressLevel;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (board != null) board.post(new Runnable() {
            @Override
            public void run() {
                board.postInit();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cornersmain);
        hideActionBar();
        initValues();
        scoreView = findViewById(R.id.corners_score);
        board = findViewById(R.id.corners_board);
        timerView = findViewById(R.id.corners_timer);
        if (board != null) {
            board.setGameEventHandler(new GameEventHandler() {
                @Override
                public void onScoreChange(int increment) {
                    score += increment;
                    moves++;
                    updateScores();
                    if (board != null) {
                        float filled = (float) board.filledTiles() / (float) board.getTilesCount();
                        if (filled <= .40) {
                            //Player is doing well, add more tiles
                            addRandomNumTiles(filled);
                        }
                    }
                }

                @Override
                public void gameOver(GameEndType type) {
                    switch (type) {
                        case Lose:
                            if (!gameLost) {
                                keepRunning = false;
                                gameLost = true;
                                if (timerView != null) timerView.pause();
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                int highScore = prefs.getInt(getString(R.string.settings_highScore), 0);
                                if (score > highScore) {
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putInt(getString(R.string.settings_highScore), score);
                                    editor.apply();
                                }
                                new GameOverDialog(CornersMain.this, score).show();
                            }
                            return;
                        case Win:
                            Toast.makeText(getApplicationContext(), "You Won!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if (timerView != null) timerView.start();
        if (savedInstanceState != null) {
            int[] arr = savedInstanceState.getIntArray(getString(R.string.settings_boardVals));
            if (board != null && arr != null)
                board.loadVals(arr);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                addLooper();
            }
        }).start();
        updateScores();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!gameLost && board != null) {
            try {
                int[] arr = board.getVals();
                outState.putIntArray(getString(R.string.settings_boardVals), arr);
            } catch (Exception e) {
                Log.e("Error saving board", e.getMessage(), e);
            }
        }
    }

    @Override
    protected void onPause() {
        keepRunning = false;
        if (timerView != null) timerView.pause();
        super.onPause();
    }

    /**
     * Add A tile to the board
     */
    public void addTile() {
        //NOTE - this will mainly be run async
        try {//Should *probably* be done on the UI Thread
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (board != null) {
                            if ((moves - maxValLastIncr) >= 30) board.increaseMaxVal();
                            board.addTile();
                        }
                    } catch (Exception e) {
                        toast(e.getMessage());
                    }
                }
            });
        } catch (final Exception e) {
            toast(e.getMessage());
        }
    }

    public void setStressLevel(StressLevel level) {
        stressLevel = level;
        LinearLayout root = findViewById(R.id.corners_root);
        switch (level) {
            case Low:
                if (root != null)
                    root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.cornersBackground_low));
                break;
            case Medium:
                if (root != null)
                    root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.cornersBackground_medium));
                break;
            case High:
                if (root != null)
                    root.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.cornersBackground_high));
                break;
        }
    }

    private void addLooper() {
        while (keepRunning) {
            try {
                Thread.sleep(time / 2);
                if (!keepRunning) return;
                addTile();
                if (time > minTime) time -= timeIncrement;
                if (time < minTime) time = minTime;
                Thread.sleep(time / 2);
            } catch (final Exception e) {
                toast(e.getMessage());
                break;
            }
        }
    }

    private void addRandomNumTiles(final float ratio) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int n = (int) (new Random().nextDouble() * (1 / ratio));
                    for (int i = 0; i < n; i++) {
                        Thread.sleep(100);
                        addTile();
                    }
                } catch (Exception e) {
                    Log.e("Error adding tiles", e.getMessage(), e);
                }
            }
        }).start();

    }

    public void pause(View view) {
        if (keepRunning) {
            keepRunning = false;
            if (board != null) board.setPaused(true);
            if (timerView != null) timerView.pause();
            ImageButton playPause = findViewById(R.id.corners_playPause);
            if (playPause != null) playPause.setImageResource(R.drawable.ic_play_arrow);
        } else {
            keepRunning = true;
            if (timerView != null) timerView.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addLooper();
                }
            }).start();
            if (board != null) board.setPaused(false);
            ImageButton playPause = findViewById(R.id.corners_playPause);
            if (playPause != null) playPause.setImageResource(R.drawable.ic_pause_white_24dp);
        }
    }

    private void updateScores() {
        if (scoreView != null)
            scoreView.setText(String.format(Locale.getDefault(), "Score: %1$d", score));
        //scoreView.setText(String.format(Locale.getDefault(), "Score: %1$d Moves: %2$d", score, moves));
    }

    @Override
    protected void initValues() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setStressLevel(StressLevel.load(prefs.getInt(getString(R.string.settings_stressLevel), StressLevel.Low.val())));
        time = prefs.getInt(getString(R.string.settings_corners_startingTime), 5000);
        timeIncrement = prefs.getInt(getString(R.string.settings_corners_timeIncrement), 100);
        score = 0;
        maxValLastIncr = 0;
        moves = 0;
        gameLost = false;
        updateScores();
    }

    @Override
    public void restart() {
        initValues();
        if (board != null) board.startOver();
        if (timerView != null) timerView.restart();
    }

    public void reset(View view) {
        restart();
    }

    public void openSettings(View view) {
        AlertDialog.Builder settingsDialog = new AlertDialog.Builder(CornersMain.this);
        final LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.corners_settings,
                (ViewGroup) findViewById(R.id.cornersSettings_root));
        final Spinner stressLevelSpinner = layout.findViewById(R.id.cornersSettings_stressLevelSpinner);
        if (stressLevelSpinner != null) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.stress_level, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stressLevelSpinner.setAdapter(adapter);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            stressLevelSpinner.setSelection(prefs.getInt(getString(R.string.settings_stressLevel), 0));
        }
        settingsDialog.setView(layout);
        settingsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                if (stressLevelSpinner != null) {
                    int x = stressLevelSpinner.getSelectedItemPosition();
                    edit.putInt(getString(R.string.settings_stressLevel), x);
                    setStressLevel(StressLevel.load(x));
                }
                edit.apply();
                dialog.dismiss();
            }
        });
        settingsDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = settingsDialog.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        .setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark));
            }
        });
        dialog.show();
    }

    public enum StressLevel {
        Low, Medium, High;

        public static StressLevel load(int level) {
            switch (level) {
                default:
                case 0:
                    return StressLevel.Low;
                case 1:
                    return StressLevel.Medium;
                case 2:
                    return StressLevel.High;
            }
        }

        public int val() {
            switch (this) {
                case Low:
                    return 0;
                case Medium:
                    return 1;
                case High:
                    return 2;
            }
            return 0;
        }
    }
}
