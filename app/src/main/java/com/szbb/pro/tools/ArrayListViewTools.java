package com.szbb.pro.tools;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.szbb.pro.adapters.CommonExpandableAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by ChanZeeBm on 2015/10/29.
 */
public class ArrayListViewTools {

    /**
     * 测量AbsListView高度(没有ScrollView作为根布局的情况下不需要用此方法)
     *
     * @param absListView
     */
    public static void measureAbsListViewHeight(ViewGroup absListView) {
        if (absListView instanceof ExpandableListView) {
            measureExpandableListViewHeight((ExpandableListView) absListView);
        } else if (absListView instanceof ListView) {
            measureListViewHeight((ListView) absListView);
        } else if (absListView instanceof RecyclerView) {

        }
    }

    /**
     * 打开expandableListView
     *
     * @param expandableListView
     */
    public static void expandGroup(ExpandableListView expandableListView, boolean isExpand) {
        if (expandableListView == null)
            return;
        if (isExpand)
            expandableListView.setGroupIndicator(null);
        ExpandableListAdapter adapter = expandableListView.getExpandableListAdapter();
        if (adapter == null) {
            Logger.i("ExpandableListAdapter is null");
            return;
        }
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    /**
     * 计算expandListView高度
     *
     * @param expandableListView
     */
    private static void measureExpandableListViewHeight(ExpandableListView expandableListView) {
        if (expandableListView == null)
            return;
        BaseExpandableListAdapter baseExpandableListAdapter = (BaseExpandableListAdapter)
                expandableListView.getExpandableListAdapter();
        if (baseExpandableListAdapter == null) {//如果没适配器或者传错对象
            return;
        }
        int totalHeight = 0;//存贮总高度
        for (int i = 0; i < baseExpandableListAdapter.getGroupCount(); i++) {//循环每一个组
            View parent = baseExpandableListAdapter.getGroupView(i, expandableListView
                    .isGroupExpanded(i), null, expandableListView);//拿到父组布局
            //根布局必须是LinearLayout
            if (!(parent instanceof LinearLayout)) {
                throw new ClassCastException("parentView must be LinearLayout");
            }
            parent.measure(0, 0);//measure一下
            totalHeight += parent.getMeasuredHeight();//父组布局总高度
            if (expandableListView.isGroupExpanded(i)) {//
                for (int j = 0; j < baseExpandableListAdapter.getChildrenCount(i); j++) {//循环子组
                    View child = baseExpandableListAdapter.getChildView(i, j, j ==
                            baseExpandableListAdapter.getChildrenCount(i), null,
                            expandableListView);
                    //根布局补习是LinearLayout,否则没有measure方法,则得不到高度
                    if (!(child instanceof LinearLayout)) {
                        throw new ClassCastException("childView must be LinearLayout");
                    }
                    //子组布局
                    child.measure(0, 0);//measure一下
                    totalHeight += child.getMeasuredHeight();//子组总高度
                }
            }
        }

        CommonExpandableAdapter adapter = (CommonExpandableAdapter) baseExpandableListAdapter;
        adapter.setGroupClickable(true);//一旦设置了总高度,该ExpandListView只能作为展示不能闭合,否则会出bug或者重新量高度(很卡
        // 不好控制)

        //设置总高度
        ViewGroup.LayoutParams params = expandableListView.getLayoutParams();
        params.height = totalHeight + (expandableListView.getDividerHeight() *
                (expandableListView.getCount() - 1));
        expandableListView.setLayoutParams(params);
    }

    /**
     * 计算listView总高度
     *
     * @param listView
     */
    private static void measureListViewHeight(ListView listView) {
        if (listView == null) {
            return;
        }
        BaseAdapter adapter = (BaseAdapter) listView.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {//循环总数
            View convertView = adapter.getView(i, null, listView);
            //根布局补习是LinearLayout,否则没有measure方法,则得不到高度
            if (!(convertView instanceof LinearLayout)) {
                throw new ClassCastException("item parentView must be LinearLayout");
            }
            convertView.measure(0, 0);
            //叠加总高度
            totalHeight += convertView.getMeasuredHeight();
        }
        //动态设置高度
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * recyclerView的线性布局显示完整高度
     */
    protected static class FullyLinearLayoutManager extends LinearLayoutManager {
        private static final String TAG = FullyLinearLayoutManager.class.getSimpleName();

        public FullyLinearLayoutManager(Context context) {
            super(context);
        }

        public FullyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        private int[] mMeasuredDimension = new int[2];

        @Override
        public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int
                widthSpec, int heightSpec) {

            final int widthMode = View.MeasureSpec.getMode(widthSpec);
            final int heightMode = View.MeasureSpec.getMode(heightSpec);
            final int widthSize = View.MeasureSpec.getSize(widthSpec);
            final int heightSize = View.MeasureSpec.getSize(heightSpec);

            int width = 0;
            int height = 0;
            for (int i = 0; i < getItemCount(); i++) {
                measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i, View
                        .MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(i, View
                        .MeasureSpec.UNSPECIFIED), mMeasuredDimension);

                if (getOrientation() == HORIZONTAL) {
                    width = width + mMeasuredDimension[0];
                    if (i == 0) {
                        height = mMeasuredDimension[1];
                    }
                } else {
                    height = height + mMeasuredDimension[1];
                    if (i == 0) {
                        width = mMeasuredDimension[0];
                    }
                }
            }
            switch (widthMode) {
                case View.MeasureSpec.EXACTLY:
                    width = widthSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            switch (heightMode) {
                case View.MeasureSpec.EXACTLY:
                    height = heightSize;
                case View.MeasureSpec.AT_MOST:
                case View.MeasureSpec.UNSPECIFIED:
            }

            setMeasuredDimension(width, height);
        }

        private void measureScrapChild(RecyclerView.Recycler recycler, int position, int
                widthSpec, int heightSpec, int[] measuredDimension) {
            try {
                View view = recycler.getViewForPosition(0);//fix 动态添加时报IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view
                            .getLayoutParams();

                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft
                            () + getPaddingRight(), p.width);

                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop
                            () + getPaddingBottom(), p.height);

                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
        }
    }
}
