package com.keithmackay.games.androidgames._2048;

import android.graphics.Color;
import android.graphics.Rect;

/**
 * Created by Keith on 2/9/2016.
 * Tile Object
 */
public class Tile {
    private final int NO_VAL = -10;
    private int v;
    private Rect b;

    public Tile() {
        b = new Rect(0, 0, 0, 0);
        v = NO_VAL;
    }

    public int getBackColor() {
        final int FF = 254;
        switch (v) {
            case 2:
                return Color.rgb(FF, 200, 0);
            case 4:
                return Color.rgb(FF, 150, 0);
            case 8:
                return Color.rgb(FF, 100, 0);
            case 16:
                return Color.rgb(FF, 50, 0);
            case 32:
                return Color.rgb(FF, 0, 0);
            case 64:
                return Color.rgb(200, 0, 50);
            case 128:
                return Color.rgb(150, 0, 100);
            case 256:
                return Color.rgb(100, 0, 150);
            case 512:
                return Color.rgb(50, 0, 200);
            case 1024:
                return Color.rgb(0, 0, FF);
            case 2048:
                return Color.rgb(0, 50, 200);
            case 4096:
                return Color.rgb(0, 100, 150);
            case 8192:
                return Color.rgb(0, 150, 100);
            case 16384:
                return Color.rgb(0, 200, 50);
            case 32768:
                return Color.rgb(0, FF, 0);
            case 65536:
                return Color.WHITE;
            case 131072:
                return Color.BLACK;
            default:
                return Color.BLACK;
        }
    }

    public int getValue() {
        return v;
    }

    public void setValue(int value) {
        if (value == 2 || (value / 2) % 2 == 0) v = value;
    }

    public boolean hasValue() {
        return v != NO_VAL;
    }

    public int removeValue() {
        int i = v;
        v = NO_VAL;
        return i;
    }

    public Rect getBounds() {
        return b;
    }

    public int getLeft() {
        return b.left;
    }

    public int getRight() {
        return b.right;
    }

    public int getTop() {
        return b.top;
    }

    public int getBottom() {
        return b.bottom;
    }

    public void setBounds(int left, int top, int right, int bottom) {
        b.left = left;
        b.top = top;
        b.right = right;
        b.bottom = bottom;
    }

    public void doubleValue() {
        v *= 2;
    }
}
