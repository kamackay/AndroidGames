package com.keithmackay.games.androidgames._2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;

import com.keithmackay.games.androidgames.R;
import com.keithmackay.games.androidgames.common.ScoreChangeHandler;

import java.util.ArrayList;

/**
 * Sixteen Block Grid
 */
public class SixteenBlockGrid extends View {
    Runnable onGameOver;
    ScoreChangeHandler scoreChangeHandler;
    /**
     * Use to print things in white
     */
    private Paint white;
    private Paint p;
    private Tile[] tiles;
    private SwipeHandler swipeHandler;

    public SixteenBlockGrid(Context context) {
        super(context);
        init(context);
    }

    public SixteenBlockGrid(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SixteenBlockGrid(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public static int getContrastColor(int color) {
        double y = (299 * Color.red(color) + 587 * Color.green(color) + 114 * Color.blue(color)) / 1000;
        return y >= 128 ? Color.BLACK : Color.WHITE;
    }

    public void init(final Context c) {
        SwipeListener swipeListener = new SwipeListener() {
            @Override
            public void onSwipe(Details details) {
                switch (details.getDirection()) {
                    case Left:
                        if (canSwipeLeft()) {
                            swipeLeft();
                            generateTile();
                            if (swipeHandler != null) swipeHandler.onLeftSwipe();
                        } else checkForGameOver();
                        postInvalidate();
                        break;
                    case Right:
                        if (canSwipeRight()) {
                            swipeRight();
                            generateTile();
                            if (swipeHandler != null) swipeHandler.onRightSwipe();
                        } else checkForGameOver();
                        postInvalidate();
                        break;
                    case Up:
                        if (canSwipeUp()) {
                            swipeUp();
                            generateTile();
                            if (swipeHandler != null) swipeHandler.onUpSwipe();
                        } else checkForGameOver();
                        postInvalidate();
                        break;
                    case Down:
                        if (canSwipeDown()) {
                            swipeDown();
                            generateTile();
                            if (swipeHandler != null) swipeHandler.onDownSwipe();
                            //if (onGameOver != null) onGameOver.run();
                        } else checkForGameOver();
                        postInvalidate();
                }
            }
        };
        swipeListener.setSwipeLength(PreferenceManager.getDefaultSharedPreferences(c)
                .getInt(c.getString(R.string.settings_swipeLen), 3));
        this.setOnTouchListener(swipeListener);


        white = new Paint();
        white.setColor(Color.WHITE);
        white.setTextSize(150);
        white.setTextAlign(Paint.Align.CENTER);
        p = new Paint();
        p.setTextSize(150);
        p.setTextAlign(Paint.Align.CENTER);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "calibri.ttf");
        if (tf != null) {
            p.setTypeface(tf);
            white.setTypeface(tf);
        }
        tiles = new Tile[16];
        for (int i = 0; i < 16; i++) tiles[i] = new Tile();
    }

    private void swipeUp() {
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 16; j += 4) {
                if (!tiles[j].hasValue()) {
                    for (int k = j + 4; k < 16; k += 4) {
                        if (tiles[k].hasValue()) {
                            tiles[j].setValue(tiles[k].getValue());
                            tiles[k].removeValue();
                            break;
                        }
                    }
                }
                if (tiles[j].hasValue()) {
                    for (int k = j + 4; k < 16; k += 4) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue()) {
                                tiles[j].doubleValue();
                                if (scoreChangeHandler != null)
                                    scoreChangeHandler.onScoreChange(tiles[j].getValue());
                                tiles[k].removeValue();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeDown() {
        for (int i = 0; i < 4; i++) {
            for (int j = i + 12; j > 0; j -= 4) {
                if (!tiles[j].hasValue()) {
                    for (int k = j - 4; k >= 0; k -= 4) {
                        if (tiles[k].hasValue()) {
                            tiles[j].setValue(tiles[k].getValue());
                            tiles[k].removeValue();
                            break;
                        }
                    }
                }
                if (tiles[j].hasValue()) {
                    for (int k = j - 4; k >= 0; k -= 4) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue()) {
                                tiles[j].doubleValue();
                                if (scoreChangeHandler != null)
                                    scoreChangeHandler.onScoreChange(tiles[j].getValue());
                                tiles[k].removeValue();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeLeft() {
        for (int i = 0; i < 16; i += 4) {
            for (int j = i; j < i + 4; j++) {
                if (!tiles[j].hasValue()) {
                    for (int k = j + 1; k < i + 4; k++) {
                        if (tiles[k].hasValue()) {
                            tiles[j].setValue(tiles[k].getValue());
                            tiles[k].removeValue();
                            break;
                        }
                    }
                }
                if (tiles[j].hasValue()) {
                    for (int k = j + 1; k < i + 4; k++) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue()) {
                                tiles[j].doubleValue();
                                if (scoreChangeHandler != null)
                                    scoreChangeHandler.onScoreChange(tiles[j].getValue());
                                tiles[k].removeValue();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    private void swipeRight() {
        for (int i = 3; i < 16; i += 4) {
            for (int j = i; j > i - 4; j--) {
                if (!tiles[j].hasValue()) {
                    for (int k = j - 1; k > i - 4; k--) {
                        if (tiles[k].hasValue()) {
                            tiles[j].setValue(tiles[k].getValue());
                            tiles[k].removeValue();
                            break;
                        }
                    }
                }
                if (tiles[j].hasValue()) {
                    for (int k = j - 1; k > i - 4; k--) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue()) {
                                tiles[j].doubleValue();
                                if (scoreChangeHandler != null)
                                    scoreChangeHandler.onScoreChange(tiles[j].getValue());
                                tiles[k].removeValue();
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void checkForGameOver() {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (getValueAt(i, j) == -10) return;
        if (!canSwipeDown() && !canSwipeLeft() && !canSwipeRight() && !canSwipeUp())
            onGameOver.run();
    }

    public void generateTile() {
        final ArrayList<Integer> emptyTiles = new ArrayList<>();

        for (int i = 0; i < 16; i++)
            if (!tiles[i].hasValue())
                emptyTiles.add(i);

        int newValue = 2;
        if (Math.random() >= 0.9) newValue = 4;

        int idx = (int) (Math.random() * (emptyTiles.size() - 1));

        final int idx2 = emptyTiles.get(idx);
        final int finalNewValue = newValue;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    tiles[idx2].setValue(finalNewValue);
                    postInvalidate();//== 1 &&
                    checkForGameOver();
                } catch (Exception e) {
                    //I don't know how to manage this
                }
            }
        }).start();
    }

    private boolean canSwipeUp() {
        for (int i = 0; i < 4; i++) {
            for (int j = i; j < 16; j += 4) {
                if (!tiles[j].hasValue()) {
                    for (int k = j + 4; k < 16; k += 4) {
                        if (tiles[k].hasValue()) {
                            return true;
                        }
                    }
                } else {
                    for (int k = j + 4; k < 16; k += 4) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue()) {
                                return true;
                            }
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canSwipeDown() {
        for (int i = 0; i < 4; i++) {
            for (int j = i + 12; j > 0; j -= 4) {
                if (!tiles[j].hasValue()) {
                    for (int k = j - 4; k >= 0; k -= 4) {
                        if (tiles[k].hasValue()) {
                            return true;
                        }
                    }
                } else {
                    for (int k = j - 4; k >= 0; k -= 4) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue())
                                return true;
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canSwipeLeft() {
        for (int i = 0; i < 16; i += 4) {
            for (int j = i; j < i + 4; j++) {
                if (!tiles[j].hasValue()) {
                    for (int k = j + 1; k < i + 4; k++) {
                        if (tiles[k].hasValue()) {
                            return true;
                        }
                    }
                } else {
                    for (int k = j + 1; k < i + 4; k++) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue())
                                return true;
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean canSwipeRight() {
        for (int i = 3; i < 16; i += 4) {
            for (int j = i; j > i - 4; j--) {
                if (!tiles[j].hasValue()) {
                    for (int k = j - 1; k > i - 4; k--)
                        if (tiles[k].hasValue()) return true;
                } else {
                    for (int k = j - 1; k > i - 4; k--) {
                        if (tiles[k].hasValue()) {
                            if (tiles[j].getValue() == tiles[k].getValue())
                                return true;
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int width = getMeasuredWidth(), height = getMeasuredHeight();
        float i = (float) (((width > height) ? height : width) * .24);
        float top = (height / 2) - (i * 2), left = (width / 2) - (i * 2),
                right = (width / 2) + (i * 2), bottom = (height / 2) + (i * 2);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int n = x * 4 + y;
                int a = (int) (left + i * y), b = (int) (top + x * i);
                tiles[n].setBounds(a, b, (int) (a + i), (int) (b + i));
                Tile t = tiles[n];
                if (t.hasValue()) {
                    int color = t.getBackColor();
                    p.setColor(color);
                    c.drawRect(t.getBounds(), p);
                    p.setColor(getContrastColor(color));
                    String s = String.valueOf(t.getValue());
                    if (s.length() > 5) p.setTextSize(50);
                    else if (s.length() == 5) p.setTextSize(60);
                    else if (s.length() == 4) p.setTextSize(75);
                    else if (s.length() == 3) p.setTextSize(100);
                    else p.setTextSize(150);
                    c.drawText(s, t.getLeft() + i / 2,
                            t.getBottom() - i / 2 + (p.getTextSize() / 3), p);
                }
            }
        }
        c.drawRect(left, top, right, top + 4, white);
        c.drawRect(left, bottom - 4, right, bottom, white);
        c.drawRect(left, top, left + 4, bottom, white);
        c.drawRect(right - 4, top, right, bottom, white);
        c.drawRect(left + i - 2, top, left + i + 2, bottom, white);
        c.drawRect(right - i - 2, top, right - i + 2, bottom, white);
        c.drawRect(left + i * 2 - 2, top, left + i * 2 + 2, bottom, white);
        c.drawRect(left, top + i - 2, right, top + i + 2, white);
        c.drawRect(left, top + i * 2 - 2, right, top + i * 2 + 2, white);
        c.drawRect(left, bottom - i - 2, right, bottom - i + 2, white);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setGridTileValue(int x, int y, int value) {
        if (tiles[y * 4 + x] != null) tiles[y * 4 + x].setValue(value);
        this.invalidate();
    }

    public void setOnSwipeHandler(SwipeHandler swipeHandler) {
        this.swipeHandler = swipeHandler;
    }

    public int getValueAt(int x, int y) {
        return tiles[y * 4 + x].getValue();
    }

    public void setOnGameOver(Runnable onGameOver) {
        this.onGameOver = onGameOver;
    }

    public Tile at(int i) {
        return tiles[i];
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i != 0 && j != 0) sb.append(", ");
                sb.append(String.valueOf(getValueAt(i, j)));
            }
        }
        return sb.toString();
    }

    public void setOnScoreChangeHandler(ScoreChangeHandler handler) {
        scoreChangeHandler = handler;
    }
}
