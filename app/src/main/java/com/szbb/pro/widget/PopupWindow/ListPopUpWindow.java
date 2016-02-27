package com.szbb.pro.widget.PopupWindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.szbb.pro.R;
import com.szbb.pro.adapters.CommonAdapter;
import com.szbb.pro.adapters.ViewHolder;
import com.szbb.pro.entity.NearbyBean;
import com.szbb.pro.impl.OnPopUpSelectListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/15.
 */
public class ListPopUpWindow extends PopupWindow implements AdapterView.OnItemClickListener,
        PopupWindow.OnDismissListener {
    private ListView parentListView;
    private ListView childListView;
    private FrameLayout flytSubLayout;
    private List<NearbyBean> parentList = new ArrayList<>();
    private List<NearbyBean> subList = new ArrayList<>();
    private List<List<NearbyBean>> subLists = new ArrayList<>();
    private CommonAdapter<NearbyBean> parentAdapter;
    private CommonAdapter<NearbyBean> childAdapter;
    private Context context;
    private Activity activity;
    private OnPopUpSelectListener listener;
    private LinearLayout parentView;
    private boolean hasChild = false;
    private int parentIndex = 0;
    private int childIndex = 0;

    public ListPopUpWindow(Context context) {
        super(context);
        this.context = context;
        this.activity = (Activity) context;
        initViews();
    }

    private void initViews() {
        parentView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.popup_listview,
                null, false);
        setContentView(parentView);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOnDismissListener(this);
        parentListView = (ListView) parentView.findViewById(R.id.listView);
        childListView = (ListView) parentView.findViewById(R.id.sub_listView);
        flytSubLayout = (FrameLayout) parentView.findViewById(R.id.flyt_sub_layout);
        flytSubLayout.setVisibility(View.GONE);
        parentAdapter = new CommonAdapter<NearbyBean>(parentList, context, R.layout.item_text) {
            @Override
            public void convert(int position, ViewHolder holder, NearbyBean bean) {
                holder.setText(R.id.text, bean.getText());
                holder.setTextColor(R.id.text, bean.getColor());
                holder.setTextCompoundsDrawable(R.id.text, bean.getDrawable());
                holder.setConvertViewBackgroundColor(R.color.color_white);
                holder.setConvertViewMinimumHeight(42);//单位是dp
                holder.setConvertViewGravity(Gravity.CENTER_VERTICAL);
                holder.setConvertViewPadding(18, 0, 0, 0);
                holder.setTextSize(R.id.text, 12);
            }
        };
        childAdapter = new CommonAdapter<NearbyBean>(subList, context, R.layout.item_text) {
            @Override
            public void convert(int position, ViewHolder holder, NearbyBean bean) {
                holder.setText(R.id.text, bean.getText());
                holder.setTextColor(R.id.text, bean.getColor());
                holder.setTextCompoundsDrawable(R.id.text, bean.getDrawable());
                holder.setConvertViewBackgroundColor(R.color.color_white);
                holder.setConvertViewMinimumHeight(42);//单位是dp
                holder.setConvertViewGravity(Gravity.CENTER_VERTICAL);
                holder.setConvertViewPadding(18, 0, 0, 0);
                holder.setTextSize(R.id.text, 12);
            }
        };
        parentListView.setAdapter(parentAdapter);
        parentListView.setSelector(R.drawable.bg_white_gravy_selector);
        parentListView.setDivider(null);
        parentListView.setDividerHeight(0);
        parentListView.setOnItemClickListener(this);

        childListView.setAdapter(childAdapter);
        childListView.setSelector(R.drawable.bg_white_gravy_selector);
        childListView.setDivider(null);
        childListView.setDividerHeight(0);
        childListView.setOnItemClickListener(this);
    }

    public void setOnPopUpSelectListener(OnPopUpSelectListener listener) {
        if (listener != null)
            this.listener = listener;
    }


    public void setTextColorAndCompoundDrawables(int position, List<NearbyBean> list) {
        list.get(position).setDrawable(context.getResources().getDrawable(R.mipmap.ic_positive));
        list.get(position).setColor(R.color.theme_primary);
        parentAdapter.notifyDataSetChanged();
    }

    public void resetTextColorAndCompoundDrawables(List<NearbyBean> list) {
        for (NearbyBean bean : list) {
            bean.setDrawable(null);
            bean.setColor(R.color.color_text_dark);
        }
        parentAdapter.notifyDataSetChanged();
        childAdapter.notifyDataSetChanged();
    }

    private void setWindowBackground(float alpha) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

    @Override
    public void showAsDropDown(@NonNull View anchor) {
        setWindowBackground(0.6f);
        super.showAsDropDown(anchor);
    }

    @Override
    public void onDismiss() {
        setWindowBackground(1f);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getId() == R.id.listView) {
            resetTextColorAndCompoundDrawables(parentList);
            setTextColorAndCompoundDrawables(position, parentList);
            parentIndex = position;
        } else if (parent.getId() == R.id.sub_listView) {
            resetTextColorAndCompoundDrawables(subList);
            setTextColorAndCompoundDrawables(position, subList);
            childIndex = position;
        }
        if (subLists != null && subLists.size() != 0 && parent.getId() == R.id.listView) {
            subList.clear();
            subList.addAll(subLists.get(position));
            childAdapter.notifyDataSetChanged();
            return;
        } else {
            childIndex = -1;
        }
        if (listener != null) {
            listener.onPopUpItemClick(parentIndex, childIndex);
        }
        dismiss();
    }

    public void setItem(List<NearbyBean> parentList) {
        hasChild = false;
        setItem(parentList, null, hasChild);

    }

    public void setItem(List<NearbyBean> parentList, List<List<NearbyBean>> childList, boolean
            hasChild) {
        this.parentList.clear();
        this.parentList.addAll(parentList);
        this.subLists.clear();
        this.hasChild = hasChild;
        if (hasChild) {
            this.subLists.addAll(childList);
            this.flytSubLayout.setVisibility(View.VISIBLE);
            this.childAdapter.notifyDataSetChanged();
        } else {
            this.flytSubLayout.setVisibility(View.GONE);
        }
        parentAdapter.notifyDataSetChanged();
    }
}
