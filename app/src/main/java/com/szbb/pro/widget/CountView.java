package com.szbb.pro.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szbb.pro.R;
import com.szbb.pro.impl.OnCountListener;

/**
 * Created by ChanZeeBm on 2015/9/30.
 */
//加减View
public class CountView<T> extends LinearLayout implements View.OnClickListener {
    private Context context;
    private TextView textView;
    private int position;
    private OnCountListener onCountListener;
    private String classifyId = "";

    private T t;


    public CountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(LinearLayout.HORIZONTAL);
        addButtons();
    }

    private void addButtons() {
        Button addButton;
        Button minusButton;

        minusButton = new Button(context);
        LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 0.9f);
        minusButton.setLayoutParams(params);
        minusButton.setId('m');
        minusButton.setText("-");
        minusButton.setOnClickListener(this);

        addButton = new Button(context);
        addButton.setLayoutParams(params);
        addButton.setId('a');
        addButton.setText("+");
        addButton.setOnClickListener(this);

        textView = new TextView(context);
        params = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        params.gravity = Gravity.CENTER;
        textView.setBackgroundColor(context.getResources().getColor(R.color.color_white));
        textView.setGravity(Gravity.CENTER);
        textView.setText("0");
        textView.setTextColor(context.getResources().getColor(R.color.color_text_gravy));
        textView.setLayoutParams(params);
        addView(minusButton, 0);
        addView(textView, 1);
        addView(addButton, 2);
    }

    public void setOnCountListener(OnCountListener l) {
        if (l != null)
            this.onCountListener = l;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int total = Integer.parseInt(textView.getText().toString());
        switch (id) {
            case 'a':
                total++;
                break;
            case 'm':
                total--;
                if (total <= 0) {
                    total = 0;
                    textView.setText(String.valueOf(total));
                    if (onCountListener != null)
                        onCountListener.onDelete(position, getT());
                    return;
                }
                break;
        }
        textView.setText(String.valueOf(total));
        if (onCountListener != null) {
            onCountListener.onCount(position, total);
        }
    }

//    public void addItem() {
//        int total = Integer.parseInt(textView.getText().toString());
//        total++;
//        textView.setText(String.valueOf(total));
//    }
//
//    public void mitigation() {
//        int total = Integer.parseInt(textView.getText().toString());
//        total--;
//        textView.setText(String.valueOf(total));
//    }

    public void setCount(int count) {
        textView.setText(String.valueOf(count));
    }

    public String getCount() {
        return textView.getText().toString();
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
