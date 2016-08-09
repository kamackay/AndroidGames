package com.keithmackay.games.androidgames.corners;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.allgames.GameActivity;

import java.util.Locale;
import java.util.Random;

public class CornersMain extends GameActivity {
    private static final int minTime = 600;
    private int time, timeIncrement, score, moves, maxValLastIncr;
    private boolean keepRunning = true;
    private CornersBoard board;
    private TextView scoreView, movesView;
    private boolean gameLost;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        time = prefs.getInt(getString(R.string.settings_corners_startingTime), 5000);
        timeIncrement = prefs.getInt(getString(R.string.settings_corners_timeIncrement), 100);
        score = 0;
        maxValLastIncr = 0;
        moves = 0;
        gameLost = false;
        scoreView = (TextView) findViewById(R.id.corners_score);
        movesView = (TextView) findViewById(R.id.corners_moves);
        board = (CornersBoard) findViewById(R.id.corners_board);
        if (board != null) {
            board.setGameEventHandler(new GameEventHandler() {
                @Override
                public void onScoreChange(int increment) {
                    score += increment;
                    moves++;
                    updateScores();
                    if (board != null) {
                        float filled = (float) board.filledTiles() / (float) board.getTilesCount();
                        if (filled <= .35) {
                            //Player is doing well, add more tiles
                            addRandomNumTiles();
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                addLooper();
            }
        }).start();
        updateScores();
    }

    @Override
    protected void onPause() {
        keepRunning = false;
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

    private static final float add2 = .85f, add3 = .9f, add4 = .95f;

    private void addRandomNumTiles() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    double r = new Random().nextDouble();
                    int n = 1;
                    if (r > add4) n = 4;
                    else if (r > add3) n = 3;
                    else if (r > add2) n = 2;
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
            ImageButton playPause = (ImageButton) findViewById(R.id.corners_playPause);
            if (playPause != null) playPause.setImageResource(android.R.drawable.ic_media_play);
        } else {
            keepRunning = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    addLooper();
                }
            }).start();
            if (board != null) board.setPaused(false);
            ImageButton playPause = (ImageButton) findViewById(R.id.corners_playPause);
            if (playPause != null) playPause.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

    private void updateScores() {
        if (scoreView != null)
            scoreView.setText(String.format(Locale.getDefault(), "Score: %1$d", score));
        if (movesView != null)
            movesView.setText(String.format(Locale.getDefault(), "Moves: %1$d", moves));
    }

    @Override
    public void restart() {
        toast("Restart - I'm useless!");
    }
}
