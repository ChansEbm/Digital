package com.szbb.pro.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.szbb.pro.BR;
import com.szbb.pro.R;

/**
 * Created by ChanZeeBm on 2016/1/18.
 */
public class Tx extends BaseObservable {
    @BindingAdapter({"app:bindColor"})
    public static void setTextColor(TextView textView, String type) {
        if (type.equals("1"))
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.color_text_red));
        else if (type.equals("2"))
            textView.setTextColor(textView.getContext().getResources().getColor(R.color.theme_primary));
    }

    private String color;

    @Bindable
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        notifyPropertyChanged(BR.color);
    }
}
