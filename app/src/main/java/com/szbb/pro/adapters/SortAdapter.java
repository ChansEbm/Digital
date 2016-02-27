package com.szbb.pro.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.szbb.pro.R;
import com.szbb.pro.databinding.ItemLendingContentBinding;
import com.szbb.pro.databinding.ItemLendingHeaderBinding;
import com.szbb.pro.entity.Lending.LendingMainClassBean;

import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/5.
 */
public class SortAdapter extends CommonBinderAdapter<LendingMainClassBean> {
    private List<LendingMainClassBean> list;
    private final static int HEAD = 0x001;
    private final static int CONTENT = 0x002;

    public SortAdapter(List<LendingMainClassBean> list) {
        super(list);
        this.list = list;
    }

    @Override
    public CommonBinderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding viewDataBinding;
        if (viewType == HEAD) {
            viewDataBinding = (DataBindingUtil.inflate(LayoutInflater.from(parent
                    .getContext()), R.layout.item_lending_header, null, false));
            return new CommonBinderHolder(viewDataBinding, viewDataBinding.getRoot(), listener);
        } else {
            viewDataBinding = (DataBindingUtil.inflate(LayoutInflater.from(parent
                    .getContext()), R.layout.item_lending_content, null, false));
            return new CommonBinderHolder(viewDataBinding, viewDataBinding.getRoot(), listener);
        }
    }

    @Override
    public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int position,
                       LendingMainClassBean lendingMainClassBean) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams
                .MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewDataBinding.getRoot().setLayoutParams(params);

        if (viewDataBinding instanceof ItemLendingHeaderBinding) {
            ((ItemLendingHeaderBinding) viewDataBinding).setHeader(lendingMainClassBean);
        } else if (viewDataBinding instanceof ItemLendingContentBinding) {
            ((ItemLendingContentBinding) viewDataBinding).setContent(lendingMainClassBean);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isHeader() ? HEAD : CONTENT;
    }
}
