package com.keithmackay.games.androidgames.corners;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.allgames.EndOfGameHandler;
import com.keithmackay.games.androidgames.allgames.GameActivity;
import com.keithmackay.games.androidgames.allgames.ScoreChangeHandler;

import java.util.Locale;

public class CornersMain extends GameActivity {
    private final int minTime = 500;
    private int time, timeIncrement, score, moves;
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
        moves = 0;
        gameLost = false;
        scoreView = (TextView) findViewById(R.id.corners_score);
        movesView = (TextView) findViewById(R.id.corners_moves);
        board = (CornersBoard) findViewById(R.id.corners_board);
        if (board != null) {
            board.setOnScoreChangeHandler(new ScoreChangeHandler() {
                @Override
                public void onScoreChange(int increment) {
                    score += increment;
                    moves++;
                    if (scoreView != null)
                        scoreView.setText(String.format(Locale.getDefault(), "Score: %1$d", score));
                    if (movesView != null)
                        movesView.setText(String.format(Locale.getDefault(), "Moves: %1$d", moves));
                }
            });
            board.setEndOfGameHandler(new EndOfGameHandler() {
                @Override
                public void gameOver(Type type) {
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
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        board.addTile();
                    } catch (Exception e) {
                        toast(e.getMessage());
                    }
                }
            });
        } catch (final Exception e) {
            toast(e.getMessage());
        }
    }


    public void pause(View view) {
        toast("Pause - I'm useless!");
    }

    @Override
    public void restart() {
        toast("Restart - I'm useless!");
    }
}
