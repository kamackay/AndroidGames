package com.keithmackay.games.androidgames.allgames;

import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class GameActivity extends AppCompatActivity {
    public void toast(final String text) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void hideActionBar() {
        ActionBar ab = getActionBar();
        if (ab != null) ab.hide();
    }

    public void sync(Runnable runnable) {
        this.runOnUiThread(runnable);
    }

    protected abstract void initVals();

    public abstract void restart();
}
