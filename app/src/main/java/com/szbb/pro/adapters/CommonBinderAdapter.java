package com.szbb.pro.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.szbb.pro.impl.BinderOnItemClickListener;
import com.szbb.pro.tools.LogTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/10/12.
 */
public abstract class CommonBinderAdapter<T> extends RecyclerView.Adapter<CommonBinderHolder> {

    protected Context context;
    protected List<T> list;
    protected int resId;
    protected BinderOnItemClickListener listener;
    protected CommonBinderHolder holder;
    protected Integer[] layouts = null;

    protected SparseArray<ViewDataBinding> parents = new SparseArray<>();

    //单个布局
    public CommonBinderAdapter(Context context, int resId, List<T> list) {
        this.context = context;
        this.list = list;
        this.resId = resId;
    }

    public CommonBinderAdapter(Context context, int resId, T[] t) {
        this.context = context;
        this.resId = resId;
        this.list = new ArrayList<>();
        for (T t1 : t) {
            list.add(t1);
        }
    }

    //多个布局
    public CommonBinderAdapter(Context context, List<T> list, Integer... layouts) {
        this.context = context;
        this.list = list;
        this.layouts = layouts;
    }

    //固定布局
    public CommonBinderAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public CommonBinderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), resId,
                parent, false);
        holder = new CommonBinderHolder(binding, binding.getRoot(), listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonBinderHolder holder, int position) {
        parents.put(position, holder.getBinding());
        onBind(holder.getBinding(), holder, position, list.get(position));
    }

    public abstract void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
            position, T t);

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ViewDataBinding getParentBinding(int pos) {
        return parents.get(pos, null);
    }

    public void setBinderOnItemClickListener(BinderOnItemClickListener listener) {
        if (listener != null)
            this.listener = listener;
    }

    public CommonBinderHolder getHolder() {
        return holder;
    }


}
