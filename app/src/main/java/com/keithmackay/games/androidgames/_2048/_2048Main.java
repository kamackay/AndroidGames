package com.keithmackay.games.androidgames._2048;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.allgames.ScoreChangeHandler;

import java.util.ArrayList;
import java.util.Locale;

public class _2048Main extends AppCompatActivity {
    public static final String VALS = "vals", SCORE_SAVE = "score", MOVES_SAVE = "moves";
    SixteenBlockGrid grid;
    private TextView tvNumberOfMoves, tvScore;
    private int moveCount, score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2048main);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        tvNumberOfMoves = (TextView) findViewById(R.id.main_numberOfMoves);
        tvScore = (TextView) findViewById(R.id.main_score);
        RelativeLayout.LayoutParams paramsMoves =
                (RelativeLayout.LayoutParams) tvNumberOfMoves.getLayoutParams(),
                paramsScore = (RelativeLayout.LayoutParams) tvScore.getLayoutParams();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
            paramsMoves.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsMoves.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsScore.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsScore.addRule(RelativeLayout.CENTER_VERTICAL);
            tvScore.setPadding(0, 0, 0, 0);
        } else {
            paramsMoves.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            paramsMoves.addRule(RelativeLayout.CENTER_HORIZONTAL);
            paramsScore.addRule(RelativeLayout.BELOW, R.id.main_numberOfMoves);
            paramsScore.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }
        tvNumberOfMoves.setLayoutParams(paramsMoves);
        tvScore.setLayoutParams(paramsScore);
        grid = (SixteenBlockGrid) findViewById(R.id.main_grid);
        if (grid != null) grid.init(getApplicationContext());
        grid.setOnGameOver(new Runnable() {
            @Override
            public void run() {
                new EndOfGameDialog(_2048Main.this).show();
            }
        });
        grid.setOnSwipeHandler(new SwipeHandler() {
            @Override
            public void onUpSwipe() {
                incrementMoves();
            }

            @Override
            public void onDownSwipe() {
                incrementMoves();
            }

            @Override
            public void onLeftSwipe() {
                incrementMoves();
            }

            @Override
            public void onRightSwipe() {
                incrementMoves();
            }
        });
        grid.setOnScoreChangeHandler(new ScoreChangeHandler() {
            @Override
            public void onScoreChange(int increment) {
                setScore(increment);
            }
        });
        score = 0;
        moveCount = 0;
        ArrayList<Integer> vals = null;
        if (savedInstanceState != null) {
            vals = savedInstanceState.getIntegerArrayList(VALS);
            score = savedInstanceState.getInt(SCORE_SAVE, 0);
            moveCount = savedInstanceState.getInt(MOVES_SAVE, 0);
        }
        if (vals == null || vals.isEmpty()) {
            grid.generateTile();
            grid.generateTile();
        } else
            for (int i = 0; i < vals.size(); i++)
                grid.setGridTileValue(i % 4, i / 4, vals.get(i));
        tvNumberOfMoves.setText(String.format(Locale.getDefault(), "Moves: %d", moveCount));
        tvScore.setText(String.format(Locale.getDefault(), "Score: %d", score));
    }

    public void incrementMoves() {
        if (tvNumberOfMoves != null)
            tvNumberOfMoves.setText(String.format(Locale.getDefault(), "Moves: %d", ++moveCount));
    }

    public void setScore(int increment) {
        score += increment;
        if (tvScore != null)
            tvScore.setText(String.format(Locale.getDefault(), "Score: %d", score));
    }

    /**
     * Save all appropriate fragment state.
     *
     * @param outState the save state bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Integer> values = new ArrayList<>();
        for (int y = 0; y < 4; y++)
            for (int x = 0; x < 4; x++)
                values.add(grid.getValueAt(x, y));
        outState.putIntegerArrayList(VALS, values);
        outState.putInt(SCORE_SAVE, score);
        outState.putInt(MOVES_SAVE, moveCount);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_2048main, menu);
        return true;
    }

    /**
     * When an option item is selected
     *
     * @param item Menu Item that was selected
     * @return pretty much always true
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void restart() {
        for (int i = 0; i < 16; i++) grid.at(i).removeValue();
        grid.postInvalidate();
        grid.generateTile();
        grid.generateTile();
        moveCount = 0;
        score = 0;
        if (tvNumberOfMoves != null)
            tvNumberOfMoves.setText(String.format(Locale.getDefault(), "Moves: %d", moveCount));
        if (tvScore != null)
            tvScore.setText(String.format(Locale.getDefault(), "Score: %d", score));
    }

    public void restart(View v) {
        restart();
    }
}
