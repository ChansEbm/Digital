package com.szbb.pro.library.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.szbb.pro.R;

import java.util.ArrayList;


/**
 * Created by Win8 on 2015/11/10.
 */
public class WheelTextAdapter extends AbstractWheelTextAdapter {

    public WheelTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxSize, int minSize) {
        super(context, R.layout.item_text, NO_RESOURCE, currentItem, maxSize, minSize, list);
        setItemTextResource(R.id.tv);
    }

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {

        return super.getItem(index, convertView, parent);
    }

    @Override
    protected CharSequence getItemText(int index) {
        return  list.get(index);
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    public String get(int currentItem) {
        return  list.get(currentItem);
    }


}
