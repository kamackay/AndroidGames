package com.keithmackay.games.androidgames.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.Locale;

/**
 * Created by kmackay on 8/10/2016.
 * View that counts time during a game
 * <p/>
 * Can be paused and unpaused as the game progresses
 */
public class GameTimer extends TextView {
    public GameTimer(Context context) {
        super(context);
        init();
    }

    public GameTimer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameTimer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private long time;

    private int time_temp;

    private void init() {
        time = 0;
        updateTime();
        time_temp = 0;
        contin = true;
    }

    /**
     * Increments time by 1 second, then updates the text
     */
    private void updateTime() {
        post(new Runnable() {
            @Override
            public void run() {
                time++;
                setText(String.format(Locale.getDefault(), "%1$d:%2$02d", time / 60, time % 60));
                invalidate();
            }
        });
    }

    private Runnable backProcess() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    while (contin) {
                        if (time_temp >= 10) {
                            updateTime();
                            time_temp = 0;
                        } else {
                            time_temp++;
                            Thread.sleep(100);
                        }
                    }
                } catch (Exception e) {
                    Log.e("Error in timer", e.getMessage(), e);
                }
            }
        };
    }

    private boolean contin;

    public void start() {
        contin = true;
        new Thread(backProcess()).start();
    }

    public void restart() {
        time = 0;
        contin = true;
        start();
    }

    public void pause() {
        contin = false;
    }
}
