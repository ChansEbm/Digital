package com.szbb.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.szbb.pro.R;
import com.szbb.pro.tools.AppTools;

/**
 * fitting status bar
 * Created by ChanZeeBm on 2016/2/19.
 */
public class StatusBar extends View {

    private Paint textPaint = new Paint();
    private Paint ovalPaint = new Paint();
    private int textSize = 20;
    private int radius = 15;
    private int totalCount = 5;
    private int lineThickness = 10;
    private boolean isCenter = true;

    private int emptyOvalColor = Color.BLACK;
    private int fillOvalColor = Color.RED;

    private int progress = 0;

    private String[] textArr;

    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StatusBar);
        textArr = getResources().getStringArray(R.array.fitting_nor);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.StatusBar_statusTextSize:
                    textSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.StatusBar_emptyColor:
                    emptyOvalColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.StatusBar_fillColor:
                    fillOvalColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.StatusBar_totalCount:
                    totalCount = a.getInteger(attr, 5);
                    textArr = new String[totalCount];
                    for (int i1 = 0; i1 < totalCount; i1++) {
                        textArr[i1] = i1 + "";
                    }
                    break;
                case R.styleable.StatusBar_radius:
                    radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.StatusBar_lineThickness:
                    lineThickness = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        textPaint.setColor(emptyOvalColor);
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        ovalPaint.setAntiAlias(true);
        ovalPaint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = AppTools.getScreenWidth();
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = AppTools.dip2px(50);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int eachLineWidth = (width / totalCount) - (radius * 2);
        int lineCount = totalCount - 1;
        drawLine(canvas, width, height, eachLineWidth, lineCount);//draw every line
        drawDot(canvas, width, height, eachLineWidth, lineCount);//draw every dot
        drawText(canvas, width, height, eachLineWidth, lineCount);//draw all text
    }

    private void drawText(Canvas canvas, int width, int height, int eachLineWidth, int lineCount) {
        for (int i = 0; i < totalCount; i++) {
            Rect rect = new Rect();
            textPaint.getTextBounds(textArr[i], 0, textArr[i].length(), rect);
            int start = 0;
            if (isCenter) {
                start = (width / 2 - ((totalCount * radius) + (lineCount * eachLineWidth)) / 2) -
                        rect.width() / 2 + radius;
            }
            int x = start + getPaddingStart() + i * eachLineWidth;
            int y = height / 2 + rect.height();
            if (i < progress) {
                textPaint.setColor(fillOvalColor);
            } else {
                textPaint.setColor(emptyOvalColor);
            }
            textPaint.setTextSize(textSize);
            canvas.drawText(textArr[i], x, y, textPaint);
        }
    }

    private void drawLine(Canvas canvas, int width, int height, int eachLineWidth, int lineCount) {
        for (int i = 0; i < lineCount; i++) {
            Rect rect = new Rect();
            textPaint.getTextBounds(textArr[i], 0, textArr[i].length(), rect);
            int start = 0;
            if (isCenter) {
                start = (width / 2 - ((totalCount * radius) + (lineCount * eachLineWidth)) / 2) +
                        radius;
            }
            int startX = start + getPaddingStart() + i * eachLineWidth;
            int endX = startX + eachLineWidth;
            int y = ((height / 2) - (rect.height() / 2));
            if (i < progress) {
                ovalPaint.setColor(fillOvalColor);
            } else {
                ovalPaint.setColor(emptyOvalColor);
            }
            ovalPaint.setStrokeWidth(lineThickness);
            canvas.drawLine(startX, y, endX, y, ovalPaint);
        }
    }

    private void drawDot(Canvas canvas, int width, int height, int eachLineWidth, int
            lineCount) {
        for (int i = 0; i < totalCount; i++) {
            Rect rect = new Rect();
            textPaint.getTextBounds(textArr[i], 0, textArr[i].length(), rect);
            int start = 0;
            if (isCenter) {
                start = width / 2 - ((totalCount * radius) + (lineCount * eachLineWidth)) / 2;
            }
            int cx = start + getPaddingStart() + radius + i * eachLineWidth;
            int cy = height / 2 - rect.height() / 2;
            if (i < progress) {
                ovalPaint.setColor(fillOvalColor);
            } else {
                ovalPaint.setColor(emptyOvalColor);
            }
            canvas.drawCircle(cx, cy, radius, ovalPaint);
        }
    }

    public void setTextArr(int arrRes) {
        this.textArr = getResources().getStringArray(arrRes);
//        if (textArr.length != totalCount && totalCount != 0) {
//            throw new NumberFormatException("err array ,array count = " + textArr.length + " " +
//                    "total count = " + totalCount);
//        }
        this.totalCount = textArr.length;
        requestLayout();
    }

    public void setEmptyOvalColor(int emptyOvalColor) {
        this.emptyOvalColor = emptyOvalColor;
        requestLayout();
    }

    public void setFillOvalColor(int fillOvalColor) {
        this.fillOvalColor = fillOvalColor;
        requestLayout();
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
        requestLayout();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        requestLayout();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        requestLayout();
    }

    public void setIsCenter(boolean isCenter) {
        this.isCenter = isCenter;
        requestLayout();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        requestLayout();
    }
}

