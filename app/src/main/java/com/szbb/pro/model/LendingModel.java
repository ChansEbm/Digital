package com.szbb.pro.model;

import android.text.TextUtils;

import com.szbb.pro.entity.Lending.LendingMainClassBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/11/6.
 */
public class LendingModel {

    public static List<LendingMainClassBean> sortHeaderList(List<LendingMainClassBean> list) {
        List<LendingMainClassBean> mList = new ArrayList<>();
        String lastHeader = "";
        for (int i = 0; i < list.size(); i++) {
            LendingMainClassBean bean = list.get(i);
            String header = bean.getFirstLetter().substring(0, 1);
            if (!TextUtils.equals(lastHeader, header)) {
                LendingMainClassBean lendingMainClassBean = new LendingMainClassBean();
                lendingMainClassBean.setText(header);
                lendingMainClassBean.setFirstLetter(header);
                lendingMainClassBean.setIsHeader(true);
                lastHeader = header;
                mList.add(lendingMainClassBean);
            }
            bean.setIsHeader(false);
            mList.add(bean);
        }
        list.clear();
        list.addAll(mList);
        return mList;
    }

}
