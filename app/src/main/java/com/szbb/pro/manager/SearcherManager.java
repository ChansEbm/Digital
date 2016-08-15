package com.szbb.pro.manager;

import com.szbb.pro.entity.order.MyOrderBean;
import com.szbb.pro.ui.fragment.order.OrderSearchBaseFragment;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/28.
 */
public class SearcherManager {
    public static void searchItems(final OrderSearchBaseFragment orderSearchBaseFragment, List<MyOrderBean.ListEntity> sourceLists, final String[] searchFields) {
        Observable.from(sourceLists).filter(new Func1<MyOrderBean.ListEntity, Boolean>() {
            @Override
            public Boolean call(MyOrderBean.ListEntity listEntity) {
                boolean isContact = false;
                for (String searchField : searchFields) {
                    isContact = listEntity.getSearchField().contains(searchField);
                }
                return isContact;
            }
        }).subscribeOn(Schedulers.newThread()).toList().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<MyOrderBean.ListEntity>>() {

            @Override
            public void call(List<MyOrderBean.ListEntity> listEntities) {
                orderSearchBaseFragment.searchResult(listEntities);
            }
        });
//        List<MyOrderBean.ListEntity> listEntities = new ArrayList<>();
//        for (MyOrderBean.ListEntity listEntity : sourceLists) {
//            String searchField = listEntity.getSearchField();
//            for (String field : searchFields) {
//                isAllContains = searchField.contains(field);
//            }
//            if (isAllContains) {
//                listEntities.add(listEntity);
//            }
//        }
//        if (listEntities.isEmpty()) {
//            Toast.makeText(orderSearchBaseFragment.getContext(),
//                    "无搜索结果", Toast.LENGTH_SHORT).show();
//        } else {
//            orderSearchBaseFragment.searchResult(listEntities);
//        }
    }
}
