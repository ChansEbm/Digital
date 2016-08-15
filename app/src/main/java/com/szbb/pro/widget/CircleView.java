package com.szbb.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.szbb.pro.R;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CircleView extends View {
    private String circleText = "0";
    private int textSize = 10;
    private Rect rect = new Rect();
    private Paint paint = new Paint();

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.CircleView_circle_text:
                    circleText = a.getString(index);
                    break;
                case R.styleable.CircleView_circle_textSize:
                    textSize = a.getDimensionPixelSize(index,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    10,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        paint.setTextSize(textSize);
        paint.getTextBounds(circleText, 0, circleText.length(), rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        int lWidth;
        int lHeight;
        if (mode != MeasureSpec.EXACTLY) {
            size = rect.width() + getPaddingRight() + getPaddingLeft();
        }
        lWidth = size;
        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode != MeasureSpec.EXACTLY) {
            size = rect.height() + getPaddingTop() + getPaddingBottom();
        }
        lHeight = size;
        int max = Math.max(lWidth, lHeight);
        setMeasuredDimension(max, max);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        int max = Math.max(measuredHeight, measuredWidth);
        float cx = max / 2;
        drawCircle(canvas, cx);
        drawText(canvas, max, cx);
    }

    private void drawText(Canvas canvas, int max, float cx) {
        paint.setTextSize(textSize);
        paint.setColor(getResources().getColor(R.color.color_white));
        paint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float baseLine = max - (max - fontHeight) / 2 - fontMetrics.bottom;
        canvas.drawText(circleText, 0, circleText.length(), cx, baseLine, paint);
    }

    private void drawCircle(Canvas canvas, float cx) {
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(getResources().getColor(R.color.color_text_red));
        canvas.drawCircle(cx, cx, cx, paint);
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        postInvalidate();
    }

    public void setCircleText(String circleText) {
        this.circleText = circleText;
        postInvalidate();
    }

    public int getUnreadCount() {
        return Integer.valueOf(circleText);
    }
}
