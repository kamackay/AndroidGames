package com.keithmackay.games.androidgames.corners;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

public class CornersBoard extends View {
    private Paint backgroundPaint;

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
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.alpha(0x000000));
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setStrokeWidth(10);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas c) {
        super.onDraw(c);
        int height = getMeasuredHeight(), width = getMeasuredWidth();
        int size = Math.min(height / 12, width / 8);
        int top = (height - size * 12) / 2;
        int left = (width - size * 8) / 2;
        c.drawLine(left, top, left + size, top + size, backgroundPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
