package com.keithmackay.games.androidgames._2048;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.keithmackay.games.androidgames.R;

/**
 * Created by Keith on 2/17/2016.
 * End of Game Dialog
 */
public class EndOfGameDialog {
    AlertDialog dialog;

    /**
     * Create the Dialog and set all needed values
     *
     * @param a the Calling Activity
     */
    public EndOfGameDialog(final MainActivity a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(a);
        final LayoutInflater inflater = (LayoutInflater.from(a));
        View v = inflater.inflate(R.layout.dialog_2048_endofgame, null);
        ((TextView) v.findViewById(R.id.dialog_endOfGame_tv))
                .setText(a.getString(R.string.endOfGame));
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
                //dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                //dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                //Wasn't as attractive as originally anticipated
            }
        });
    }

    public void show() {
        dialog.show();
    }
}
