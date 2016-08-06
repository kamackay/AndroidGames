package com.keithmackay.games.androidgames;

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

    public void sync(Runnable runnable){
        this.runOnUiThread(runnable);
    }
}
