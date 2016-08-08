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
import com.keithmackay.games.androidgames.allgames.EndOfGameHandler;
import com.keithmackay.games.androidgames.allgames.ScoreChangeHandler;

import java.util.Random;

public class CornersBoard extends View {
    private final int padding = 3;
    EndOfGameHandler endOfGameHandler;
    ScoreChangeHandler scoreChangeHandler;
    private Paint backgroundPaint;
    private Paint[] tileColors, loadingTileColors;
    private Bitmap bmapError;
    private int maxVal = 8, tileLoadTime;
    private Tile[][] tiles;
    private int colCount = 8, rowCount = 12;

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

    private void init(Context c) {
        tileLoadTime = 5000;
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.GRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setStrokeWidth(10);

        int colors = 8;

        tileColors = new Paint[colors];
        loadingTileColors = new Paint[colors];
        for (int i = 0; i < colors; i++) {
            int col = getColor(i);
            tileColors[i] = new Paint();
            tileColors[i].setColor(col);
            tileColors[i].setStyle(Paint.Style.FILL);

            loadingTileColors[i] = new Paint();
            loadingTileColors[i].setColor(Color.argb(0x80, Color.red(col), Color.green(col), Color.blue(col)));
            loadingTileColors[i].setStyle(Paint.Style.FILL);
        }

        final int[] numFilled = {0};
        final int max = (colCount * rowCount) / 2;
        final Random rand = new Random();

        tiles = new Tile[colCount][rowCount];
        new Thread(new Runnable() {
            @Override
            public void run() {
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
        }).start();

        setOnTouchListener(new OnTouchListener() {
            private float downX, downY;
            private long downTime;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = event.getX();
                        downY = event.getY();
                        downTime = System.currentTimeMillis();
                        return true;
                    case MotionEvent.ACTION_UP:
                        float upX = event.getX(), upY = event.getY();
                        int height = getMeasuredHeight(), width = getMeasuredWidth();
                        int size = Math.min(height / rowCount, width / colCount);
                        int top = (height - size * rowCount) / 2;
                        int left = (width - size * colCount) / 2;
                        //Find the column and that this would be in
                        int col = 0, row = 0;
                        while (upX > (left + ((col + 1) * size))) col++;
                        while (upY > (top + ((row + 1) * size))) row++;
                        if (!tiles[col][row].isEmptyAndLoaded()) return true;
                        //Toast.makeText(getContext(), String.format(Locale.getDefault(), "Column - %1$d   Row - %2$d", col, row), Toast.LENGTH_SHORT).show();
                        TileInfo tileLeft = findLeft(col, row), tileUp = findUp(col, row), tileDown = findDown(col, row), tileRight = findRight(col, row);
                        int scoreDelta = 0;
                        if (!tileLeft.nothing && (tileLeft.val == tileUp.val || tileLeft.val == tileDown.val || tileLeft.val == tileRight.val)) {
                            tiles[tileLeft.x][tileLeft.y].clearVal();
                            scoreDelta++;
                        }
                        if (!tileUp.nothing && (tileUp.val == tileLeft.val || tileUp.val == tileDown.val || tileUp.val == tileRight.val)) {
                            tiles[tileUp.x][tileUp.y].clearVal();
                            scoreDelta++;
                        }
                        if (!tileDown.nothing && (tileDown.val == tileLeft.val || tileDown.val == tileUp.val || tileDown.val == tileRight.val)) {
                            tiles[tileDown.x][tileDown.y].clearVal();
                            scoreDelta++;
                        }
                        if (!tileRight.nothing && (tileRight.val == tileLeft.val || tileRight.val == tileUp.val || tileRight.val == tileDown.val)) {
                            tiles[tileRight.x][tileRight.y].clearVal();
                            scoreDelta++;
                        }
                        if (scoreDelta > 0) scoreChangeHandler.onScoreChange(scoreDelta);
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

    protected TileInfo findRight(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = x + 1;
        while (temp < colCount) {
            if (!tiles[temp][y].isEmptyAndLoaded()) {
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

    protected TileInfo findDown(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = y + 1;
        while (temp < rowCount) {
            if (!tiles[x][temp].isEmptyAndLoaded()) {
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

    protected TileInfo findUp(int x, int y) {
        TileInfo ti = new TileInfo();
        int temp = y - 1;
        while (temp >= 0) {
            if (!tiles[x][temp].isEmptyAndLoaded()) {
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
            if (!tiles[temp][y].isEmptyAndLoaded()) {
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
            endOfGameHandler.gameOver(EndOfGameHandler.Type.Lose);
        } else {
            tiles[x][y] = new Tile();
            tiles[x][y].setVal(rand.nextInt(maxVal) + 1);
            tiles[x][y].setLoading(true);
            final int finX = x, finY = y;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(tileLoadTime);
                        tiles[finX][finY].loading = false;
                        postInvalidate();
                    } catch (Exception e) {
                        Log.e("Couldn't unload tile", e.getMessage(), e);
                    }
                }
            }).start();
            invalidate();
        }
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
                    Tile temp = tiles[x][y];
                    int tempY = top + (size * y) + padding, tempX = left + (size * x) + padding;
                    if (temp.isEmpty()) {
                        c.drawRect(tempX, tempY, tempX + (size - padding), tempY + (size - padding), backgroundPaint);
                    } else {
                        c.drawRect(tempX, tempY, tempX + (size - padding), tempY + (size - padding),
                                temp.isLoading() ? loadingTileColors[temp.getVal() - 1] : tileColors[temp.getVal() - 1]);
                    }
                    if (temp.err) {
                        c.drawRect(tempX, tempY, tempX + (size - padding), tempY + (size - padding), backgroundPaint);
                        c.drawBitmap(bmapError, tempX + (int) (size * .125), tempY + (int) (size * .125), backgroundPaint);
                    }
                }
            }/**/
        } catch (Exception e) {
            Log.e("Error drawing tiles", e.getMessage(), e);
        }
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
                return Color.MAGENTA;
            case 4:
                return Color.RED;
            case 5:
                return Color.CYAN;
            case 6:
                return Color.argb(0x80, 0xff, 0x69, 0xb4);
            case 7:
                return Color.argb(0x80, 0xff, 0x8c, 0x0);
            case 8:
                return Color.GRAY;
        }
    }

    public void postInit() {
        int height = getMeasuredHeight(), width = getMeasuredWidth();
        int size = Math.min(height / rowCount, width / colCount);
        bmapError = Bitmap.createScaledBitmap(bmapError, (int) (size * .75), (int) (size * .75), false);
    }

    public void setOnScoreChangeHandler(ScoreChangeHandler handler) {
        scoreChangeHandler = handler;
    }

    public void setEndOfGameHandler(EndOfGameHandler handler) {
        endOfGameHandler = handler;
    }

    public class TileInfo {
        public int x, y, val;
        public boolean nothing = false;
    }

    public class Tile {
        public boolean err;
        private boolean loading = false;
        private int val = -1;

        public Tile() {
            err = false;
        }

        public boolean isEmpty() {
            return val == -1;
        }

        public boolean isEmptyAndLoaded() {
            return val == -1 && !loading;
        }

        public boolean isLoading() {
            return loading;
        }

        public void setLoading(boolean loading) {
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
