package com.keithmackay.games.androidgames.corners;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.common.GameActivity;

import java.util.Locale;

/**
 * Created by Keith on 2/17/2016.
 * End of Game Dialog
 */
public class GameOverDialog {
    AlertDialog dialog;

    /**
     * Create the Dialog and set all needed values
     *
     * @param a the Calling Activity
     */
    public GameOverDialog(final GameActivity a, int finalScore) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        final LayoutInflater inflater = (LayoutInflater.from(a));
        View v = inflater.inflate(R.layout.dialog_gameover, null);
        TextView title = (TextView) v.findViewById(R.id.dialog_gameOver_title);
        if (title != null) title.setText("Game Over!");
        TextView content = (TextView) v.findViewById(R.id.dialog_gameOver_content);
        if (content != null)
            content.setText(String.format(Locale.getDefault(), "Final Score: %1$d\nHigh Score: %2$d",
                    finalScore,
                    PreferenceManager.getDefaultSharedPreferences(a.getApplicationContext())
                            .getInt(a.getString(R.string.settings_highScore), finalScore)));
        builder.setView(v);
        builder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                a.restart();
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                a.finish();
            }
        });
        dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                //Wasn't as attractive as originally anticipated
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
