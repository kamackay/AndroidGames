package com.keithmackay.games.androidgames.render;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.common.GameActivity;

public class RenderMain extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_render);

    }

    @Override
    protected void initValues() {

    }

    @Override
    public void restart() {

    }
}
