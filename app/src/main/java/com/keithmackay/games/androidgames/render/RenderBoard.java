package com.keithmackay.games.androidgames.render;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class RenderBoard extends View {
    public RenderBoard(Context context) {
        super(context);
        init();
    }

    public RenderBoard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.WHITE);
        touchLocations = new ArrayList<>();
    }

    private Paint paint;
    private List<Point> touchLocations;

    @SuppressLint("NewApi")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_MOVE:
            case MotionEvent.ACTION_MOVE:
                touchLocations = toPoints(event);
                invalidate();
                return false;
            case MotionEvent.ACTION_DOWN:
                touchLocations = toPoints(event);
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                touchLocations.clear();
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    private List<Point> toPoints(MotionEvent event) {
        touchLocations.clear();
        for (int x = 0; x < Math.min(event.getPointerCount(), 2); x++) {
            touchLocations.add(new Point((int) event.getX(x), (int) event.getY(x)));
        }
        return touchLocations;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Point location : touchLocations) {
            canvas.drawCircle(location.x, location.y, 50, paint);
        }
    }
}
