package com.szbb.pro.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.szbb.pro.tools.AppTools;

/**
 * Created by ChanZeeBm on 2015/9/8.
 */
public class ViewHolder {
    private View convertView;
    private int position;
    private SparseArray<View> views;
    private Context context;
    private ViewDataBinding viewDataBinding;

    private ViewHolder(Context context, ViewGroup viewGroup, int resId, int position) {
        views = new SparseArray<>();
        this.position = position;
        this.context = context;
        convertView = LayoutInflater.from(context).inflate(resId, viewGroup, false);
        viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(context), resId, viewGroup,
                false);
        convertView.setTag(this);
    }

    public static ViewHolder get(Context context, View convertView, ViewGroup viewGroup, int
            resId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, viewGroup, resId, position);
        } else {
            return (ViewHolder) convertView.getTag();
        }
    }

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    public void setText(int id, String text) {
        TextView textView = getViews(id);
        textView.setText(text);
    }

    public void setTextColor(int id, int color) {
        int colorRes = context.getResources().getColor(color);
        TextView textView = getViews(id);
        textView.setTextColor(colorRes);
    }

    public void setTextSize(int id, int size) {
        TextView textView = getViews(id);
        textView.setTextSize(size);
    }

    public void setTextGravity(int id, int gravity) {
        TextView textView = getViews(id);
        textView.setGravity(gravity);
    }

    public void setTextCompoundsDrawable(int id, Drawable drawable) {
        TextView textView = getViews(id);
        if (drawable == null) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
    }

    public void setConvertViewBackgroundColor(int color) {
        View v = getConvertView();
        int colorRes = context.getResources().getColor(color);
        v.setBackgroundColor(colorRes);
    }

    public void setConvertViewBackgroundRes(int res) {
        View v = getConvertView();
        v.setBackgroundResource(res);
    }

    public void setConvertViewMinimumHeight(int height) {
        View view = getConvertView();
        int minHeightPx = AppTools.dip2px(height);
        view.setMinimumHeight(minHeightPx);
    }

    public void setConvertViewGravity(int gravity) {
        LinearLayout linearLayout = (LinearLayout) getConvertView();
        linearLayout.setGravity(gravity);
    }

    public void setConvertViewPaddingRes(int lRes, int tRes, int rRes, int bRes) {
        View view = getConvertView();
        int l, t, r, b;
        l = t = r = b = 0;
        if (lRes != 0)
            l = (int) context.getResources().getDimension(lRes);
        if (tRes != 0)
            t = (int) context.getResources().getDimension(tRes);
        if (rRes != 0)
            r = (int) context.getResources().getDimension(rRes);
        if (bRes != 0)
            b = (int) context.getResources().getDimension(bRes);
        view.setPadding(l, t, r, b);
    }

    public void setConvertViewPadding(int l, int t, int r, int b) {
        View view = getConvertView();
        view.setPadding(l, t, r, b);
    }


    public void setImageResource(int id, int resId) {
        ImageView imageView = getViews(id);
        imageView.setImageResource(resId);
    }

    public void setMinHeight(int id, int height) {
        View v = getViews(id);
        v.setMinimumHeight(height);
    }


    public void setPadding(int id, int left, int top, int right, int bottom) {
        View v = getViews(id);
        v.setPadding(left, top, right, bottom);
    }

    public void displayImage(int id, String uri) {
        SimpleDraweeView simpleDraweeView = getViews(id);
        simpleDraweeView.setImageURI(Uri.parse(uri));
    }

    /**
     * get views
     *
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T getViews(int id) {
        View view = views.get(id);
        if (view == null) {
            view = convertView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return convertView;
    }

    public int getPosition() {
        return position;
    }


}
