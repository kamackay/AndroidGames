package com.keithmackay.games.androidgames.corners;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.keithmackay.games.androidgames.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CornersBoard extends View {
    private final int padding = 3;
    GameEventHandler gameEventHandler;
    private Paint backgroundPaint, whitePaint;
    private Paint[] tileColors, loadingTileColors;
    private Bitmap bmapError;
    private int maxVal;
    private int tileLoadTime;
    private static final int ABSOLUTE_MAXVAL = 9;
    private Tile[][] tiles;
    private int colCount = 8, rowCount = 12;
    private boolean paused;
    List<Bitmap> backgrounds;

    public CornersBoard(Context context) {
        super(context);
        init(context);
    }

    public CornersBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CornersBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CornersBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private static final int COLORS_COUNT = 8;

    private void init(Context c) {
        paused = false;

        tileLoadTime = 500;

        maxVal = COLORS_COUNT;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setStrokeWidth(10);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        tileColors = new Paint[COLORS_COUNT];
        loadingTileColors = new Paint[COLORS_COUNT];
        for (int i = 0; i < COLORS_COUNT; i++) {
            int col = getColor(i);
            tileColors[i] = new Paint();
            tileColors[i].setColor(col);
            tileColors[i].setStyle(Paint.Style.FILL);

            loadingTileColors[i] = new Paint();
            loadingTileColors[i].setColor(Color.argb(0x80, Color.red(col), Color.green(col), Color.blue(col)));
            loadingTileColors[i].setStyle(Paint.Style.FILL);
        }

        tiles = new Tile[colCount][rowCount];
        setupTiles();

        backgrounds = new ArrayList<>();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_UP:
                        float upX = event.getX(), upY = event.getY();
                        if (paused) return true;
                        int height = getMeasuredHeight(), width = getMeasuredWidth();
                        int size = Math.min(height / rowCount, width / colCount);
                        int top = (height - size * rowCount) / 2;
                        int left = (width - size * colCount) / 2;
                        //Find the column and that this would be in
                        int col = 0, row = 0;
                        while (upX > (left + ((col + 1) * size))) col++;
                        while (upY > (top + ((row + 1) * size))) row++;
                        if (!tiles[col][row].isEmptyOrLoading()) return true;
                        //Toast.makeText(getContext(), String.format(Locale.getDefault(), "Column - %1$d   Row - %2$d", col, row), Toast.LENGTH_SHORT).show();
                        TileInfo tileLeft = findLeft(col, row), tileUp = findUp(col, row), tileDown = findDown(col, row), tileRight = findRight(col, row);
                        int scoreDelta = 0;
                        if (!tileLeft.nothing && (tileLeft.val == tileUp.val || tileLeft.val == tileDown.val || tileLeft.val == tileRight.val)) {
                            scoreDelta += tiles[tileLeft.x][tileLeft.y].getVal() > (COLORS_COUNT + 1) ?
                                    2 * Math.abs(col - tileLeft.x) : Math.abs(col - tileLeft.x);
                            tiles[tileLeft.x][tileLeft.y].clearVal();
                        }
                        if (!tileUp.nothing && (tileUp.val == tileLeft.val || tileUp.val == tileDown.val || tileUp.val == tileRight.val)) {
                            scoreDelta += tiles[tileUp.x][tileUp.y].getVal() > (COLORS_COUNT + 1) ?
                                    2 * Math.abs(row - tileUp.y) : Math.abs(row - tileUp.y);
                            tiles[tileUp.x][tileUp.y].clearVal();
                        }
                        if (!tileDown.nothing && (tileDown.val == tileLeft.val || tileDown.val == tileUp.val || tileDown.val == tileRight.val)) {
                            scoreDelta += tiles[tileDown.x][tileDown.y].getVal() > (COLORS_COUNT + 1) ?
                                    2 * Math.abs(row - tileDown.y) : Math.abs(row - tileDown.y);
                            tiles[tileDown.x][tileDown.y].clearVal();
                        }
                        if (!tileRight.nothing && (tileRight.val == tileLeft.val || tileRight.val == tileUp.val || tileRight.val == tileDown.val)) {
                            scoreDelta += tiles[tileRight.x][tileRight.y].getVal() > (COLORS_COUNT + 1) ?
                                    2 * Math.abs(col - tileRight.x) : Math.abs(col - tileRight.x);
                            tiles[tileRight.x][tileRight.y].clearVal();
                        }
                        if (scoreDelta > 0) gameEventHandler.onScoreChange(scoreDelta);
                        else {
                            tiles[col][row].err = true;
                            final int finCol = col, finRow = row;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(500);
                                        tiles[finCol][finRow].err = false;
                                        postInvalidate();
                                    } catch (Exception e) {
                                        Log.e("Error hiding X image", e.getMessage(), e);
                                    }
                                }
                            }).start();
                            //TODO: Add two more tiles? / Lower the score?
                        }
                        invalidate();
                        return true;
                }
                return false;
            }
        });
        bmapError = BitmapFactory.decodeResource(c.getResources(), R.drawable.err);
    }

    public void startOver() {
        maxVal = COLORS_COUNT;
        paused = false;
        setupTiles();
        postInvalidate();
    }

    private void setupTiles() {
        final int[] numFilled = {0};
        final int max = (colCount * rowCount) / 2;
        final Random rand = new Random();
        for (int x = 0; x < colCount && numFilled[0] < max; x++) {
            for (int y = 0; y < rowCount; y++) {
                tiles[x][y] = new Tile();
                if (rand.nextBoolean()) {
                    tiles[x][y].setVal(rand.nextInt(maxVal) + 1);
                    numFilled[0]++;
                }
            }
        }
    }

    protected TileInfo findRight(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = x + 1;
        while (temp < colCount) {
            if (!tiles[temp][y].isEmptyOrLoading()) {
                ti.x = temp;
                ti.y = y;
                ti.val = tiles[temp][y].getVal();
                return ti;
            }
            temp++;
        }
        ti.nothing = true;
        return ti;
    }

    /**
     * Quit reading my documentation
     *
     * @param x the x index of the clicked tile
     * @param y the y index of the clicked tile
     * @return the TileInfo of the fist non-empty tile
     */
    protected TileInfo findDown(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = y + 1;
        while (temp < rowCount) {
            if (!tiles[x][temp].isEmptyOrLoading()) {
                ti.x = x;
                ti.y = temp;
                ti.val = tiles[x][temp].getVal();
                return ti;
            }
            temp++;
        }
        ti.nothing = true;
        return ti;
    }

    public void increaseMaxVal() {
        if (maxVal < ABSOLUTE_MAXVAL) maxVal++;
    }

    public int getMaxVal() {
        return maxVal;
    }

    protected TileInfo findUp(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = y - 1;
        while (temp >= 0) {
            if (!tiles[x][temp].isEmptyOrLoading()) {
                ti.x = x;
                ti.y = temp;
                ti.val = tiles[x][temp].getVal();
                return ti;
            }
            temp--;
        }
        ti.nothing = true;
        return ti;
    }

    protected TileInfo findLeft(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = x - 1;
        while (temp >= 0) {
            if (!tiles[temp][y].isEmptyOrLoading()) {
                ti.x = temp;
                ti.y = y;
                ti.val = tiles[temp][y].getVal();
                return ti;
            }
            temp--;
        }
        ti.nothing = true;
        return ti;
    }

    public void addTile() {
        Random rand = new Random();
        int x = rand.nextInt(colCount), y = rand.nextInt(rowCount);
        boolean found = false;
        searchForEmpty:
        {
            for (int i = x; i < colCount; i++) {
                for (int j = y; j < rowCount; j++) {
                    if (tiles[i][j].isEmpty()) {
                        x = i;
                        y = j;
                        found = true;
                        break searchForEmpty;
                    }
                }
            }
            for (int i = 0; i < colCount; i++) {
                for (int j = 0; j < rowCount; j++) {
                    if (tiles[i][j].isEmpty()) {
                        x = i;
                        y = j;
                        found = true;
                        break searchForEmpty;
                    }
                }
            }
        }
        if (!found) {
            //Lose the game, all spots are filled
            gameEventHandler.gameOver(GameEventHandler.GameEndType.Lose);
        } else {
            tiles[x][y] = new Tile();
            final int val = rand.nextInt(maxVal) + 1;
            tiles[x][y].setVal(val);
            tiles[x][y].setLoading(0);
            final int finX = x, finY = y;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (val <= COLORS_COUNT) {
                            final float animationIncrement = .05f;
                            final int sleepTime = (int) (tileLoadTime * animationIncrement);
                            //Thread.sleep(tileLoadTime);
                            //tiles[finX][finY].setLoading(1);
                            for (float i = 0; i <= 1; i += animationIncrement) {
                                Thread.sleep(sleepTime);
                                tiles[finX][finY].setLoading(i);
                                postInvalidate();
                            }
                        } else {
                            Thread.sleep(tileLoadTime);
                        }
                        tiles[finX][finY].setLoading(1);
                        postInvalidate();
                    } catch (Exception e) {
                        Log.e("Couldn't load tile", e.getMessage(), e);
                    }
                }
            }).start();
            postInvalidate();
        }
    }

    /**
     * Set whether or not the game is paused
     *
     * @param pauseState true if the game is paused, false otherwise
     */
    public void setPaused(boolean pauseState) {
        paused = pauseState;
    }

    public int[] getVals() {
        int[] vals = new int[colCount * rowCount];
        for (int x = 0; x < colCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                vals[x + (y * colCount)] = tiles[x][y].getVal();
            }
        }
        return vals;
    }

    public void loadVals(int[] vals) {
        for (int x = 0; x < colCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                tiles[x][y].setVal(vals[x + (y * colCount)]);
            }
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int height = getMeasuredHeight(), width = getMeasuredWidth();
        int size = Math.min(height / rowCount, width / colCount);
        int top = (height - size * rowCount) / 2;
        int left = (width - size * colCount) / 2;/**/
        try {
            for (int x = 0; x < colCount; x++) {
                for (int y = 0; y < rowCount; y++) {
                    try {
                        Tile temp = tiles[x][y];
                        int tempY = top + (size * y) + padding, tempX = left + (size * x) + padding;
                        if (temp == null || temp.isEmpty()) {
                            c.drawRect(tempX, tempY, tempX + (size - padding),
                                    tempY + (size - padding), backgroundPaint);
                        } else if (temp.isLoading() && temp.getVal() < COLORS_COUNT + 1) {
                            Paint p = loadingTileColors[temp.getVal() - 1];
                            p.setAlpha((int) (0xFF * temp.getLoadingProgress()));
                            c.drawRect(tempX, tempY, tempX + (size - padding),
                                    tempY + (size - padding), backgroundPaint);
                            c.drawRect(tempX, tempY, tempX + (size - padding),
                                    tempY + (size - padding), p);
                        } else if (temp.getVal() < COLORS_COUNT + 1) {
                            c.drawRect(tempX, tempY, tempX + (size - padding),
                                    tempY + (size - padding), tileColors[(temp.getVal() - 1) % COLORS_COUNT]);
                        } else {
                            Bitmap b = backgrounds.get(temp.getVal() - (COLORS_COUNT + 1));
                            c.drawBitmap(b, tempX, tempY, backgroundPaint);
                        }
                        if (temp != null && temp.err) {
                            c.drawRect(tempX, tempY, tempX + (size - padding),
                                    tempY + (size - padding), backgroundPaint);
                            c.drawBitmap(bmapError, tempX + (int) (size * .125),
                                    tempY + (int) (size * .125), backgroundPaint);
                        }
                    } catch (Exception e) {
                        Log.e("Error during draw", e.getMessage(), e);
                    }
                }
            }/**/
        } catch (Exception e) {
            Log.e("Error drawing tiles", e.getMessage(), e);
        }
    }

    /**
     * Get the number of tiles that are filled on the board
     *
     * @return the number of tiles that are filled on the board
     */
    public int filledTiles() {
        int n = 0;
        for (int x = 0; x < colCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                if (!tiles[x][y].isEmpty()) n++;
            }
        }
        return n;
    }

    /**
     * Get the total number of tiles (filled and empty) on the board
     *
     * @return the column count multiplied by the row count
     */
    public int getTilesCount() {
        return rowCount * colCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int getColor(int val) {
        switch (val) {
            default:
                return Color.BLUE;
            case 1:
                return Color.YELLOW;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.argb(0xFF, 0x0, 0x78, 0x0);
            case 4:
                return Color.RED;
            case 5:
                return Color.CYAN;
            case 6:
                return Color.MAGENTA;
            //return Color.argb(0xff, 0xff, 0x69, 0xb4);
            case 7:
                return Color.argb(0xff, 0xff, 0x8c, 0x0);
            case 8:
                return Color.GRAY;
        }
    }

    public void postInit() {
        int height = getMeasuredHeight(), width = getMeasuredWidth();
        int size = Math.min(height / rowCount, width / colCount);
        bmapError = Bitmap.createScaledBitmap(bmapError, (int) (size * .75), (int) (size * .75), false);
        backgrounds.add(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
                getContext().getResources(), R.drawable.zigzag_yellow_small),
                size - (padding * 2), size - (padding * 2), false));
    }

    public void setGameEventHandler(GameEventHandler handler) {
        gameEventHandler = handler;
    }

    public class TileInfo {
        public int x, y, val;
        public boolean nothing = false;
    }

    public class Tile {
        public boolean err;
        private float loading;
        private int val = -1;

        public Tile() {
            err = false;
            loading = 1;
        }

        public float getLoadingProgress() {
            return loading;
        }

        public boolean isEmpty() {
            return val == -1;
        }

        public boolean isEmptyOrLoading() {
            return val == -1 || loading != 1;
        }

        public boolean isLoading() {
            return loading != 1;
        }

        public void setLoading(float loading) {
            this.loading = loading;
        }

        public void clearVal() {
            val = -1;
        }

        public int getVal() {
            return val;
        }

        public void setVal(int newVal) {
            val = newVal;
        }
    }
}
