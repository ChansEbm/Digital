package com.szbb.pro.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;

import com.szbb.pro.R;

/**
 * Created by ChanZeeBm on 2016/3/9.
 */
public class FancyIndicator extends View {
    private Paint cyclePaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint drawablePaint = new Paint();

    private RecyclerView recyclerView;
    private int radius = 10;
    private int lineWidth = 5;
    private int progress = 2;

    private int chosenColor = Color.BLACK;
    private int normalColor = Color.GRAY;

    public FancyIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        initCustomer(context, attrs);
        initPaints();
    }

    private void initCustomer(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FancyIndicator);
        final int indexCount = a.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.FancyIndicator_chosenColor:
                    this.chosenColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.FancyIndicator_fancy_radius:
                    this.radius = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.FancyIndicator_fancy_lineThickness:
                    this.lineWidth = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension
                            (TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.FancyIndicator_normalColor:
                    this.normalColor = a.getColor(attr, Color.GRAY);
                    break;
            }
        }
        a.recycle();
    }

    private void initPaints() {
        cyclePaint.setAntiAlias(true);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        drawablePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size;
        int mode;
        int width;
        int height;
        size = MeasureSpec.getSize(widthMeasureSpec);
        mode = MeasureSpec.getMode(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = 100;
        }
        size = MeasureSpec.getSize(heightMeasureSpec);
        mode = MeasureSpec.getMode(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = 250;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            final int itemCount = recyclerView.getAdapter().getItemCount();
            Rect currentRect = new Rect();
            recyclerView.getGlobalVisibleRect(currentRect);
            for (int i = 0; i < itemCount; i++) {
                View currentChild = recyclerView.getLayoutManager().findViewByPosition(i);
                int nextPos = i + 1;
                if (nextPos >= itemCount) {
                    nextPos = i;
                }
                View nextChild = recyclerView.getLayoutManager().findViewByPosition(nextPos);
                if (currentChild == null || nextChild == null) {
                    return;
                }
                int[] current = new int[2];
                int[] next = new int[2];
                currentChild.getLocationOnScreen(current);
                nextChild.getLocationOnScreen(next);
                int currentBottom = current[1] + currentChild.getHeight();
                int currentTop = current[1];
                int nextBottom = next[1] + nextChild.getHeight();
                int nextTop = next[1];
                int currentYPos = currentBottom - ((currentBottom - currentTop) / 2) -
                        currentRect.top;
                int nextYPos = nextBottom - ((nextBottom - nextTop) / 2) - currentRect.top;

                int cx = getMeasuredWidth() / 2;
                if (i < progress) {
                    cyclePaint.setColor(chosenColor);
                    linePaint.setColor(chosenColor);
                } else {
                    cyclePaint.setColor(normalColor);
                    linePaint.setColor(normalColor);
                }
                if (i != progress)
                    canvas.drawCircle(cx, currentYPos, radius, cyclePaint);
                if (i != nextPos)
                    canvas.drawLine(cx, currentYPos, cx, nextYPos, linePaint);
                if (i == progress) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap
                            .ic_track);
                    int left = cx - (bitmap.getWidth() / 2);
                    int top = currentYPos - (bitmap.getHeight() / 2);
                    canvas.drawBitmap(bitmap, left, top, drawablePaint);
                }
            }
        }
    }

    public void attachRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                FancyIndicator.this.postInvalidate();
                FancyIndicator.this.recyclerView.getViewTreeObserver()
                        .removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        postInvalidate();
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        postInvalidate();
    }

    public void setChosenColor(int chosenColor) {
        this.chosenColor = chosenColor;
        postInvalidate();
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        postInvalidate();
    }

    @Override
    public void postInvalidate() {
        if (recyclerView != null)
            super.postInvalidate();
        else
            throw new RuntimeException("RecyclerView didn't set");

    }
}
