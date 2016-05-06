package com.szbb.pro.ui.fragment.main;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.szbb.pro.R;
import com.szbb.pro.RefreshRecyclerLayout;
import com.szbb.pro.adapters.CommonBinderAdapter;
import com.szbb.pro.adapters.CommonBinderHolder;
import com.szbb.pro.base.BaseFgm;
import com.szbb.pro.databinding.FgmNearbyBinding;
import com.szbb.pro.databinding.ItemNearbyBinding;
import com.szbb.pro.entity.NearbyBean;
import com.szbb.pro.entity.NearbyContentBean;
import com.szbb.pro.eum.TopSelection;
import com.szbb.pro.impl.OnPopUpSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.widget.PopupWindow.ListPopUpWindow;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChanZeeBm on 2015/9/16.
 */
public class NearbyFragment extends BaseFgm implements BDLocationListener, OnPopUpSelectListener {

    private List<NearbyBean> parentList = new ArrayList<>();
    private List<List<NearbyBean>> childList = new ArrayList<>();
    private List<NearbyContentBean> list = new ArrayList<>();

    private ListPopUpWindow listPopUpWindow;
    private TopSelection topSelection;
    private int[] topFlyts = {R.id.flyt_nearby_top_nearby, R.id.flyt_nearby_top_classify, R.id
            .flyt_nearby_top_sort, R.id.flyt_nearby_top_price};
    private TextView tvNearbyLocation;
    private View rootView;
    private FgmNearbyBinding fgmNearbyBinding;
    private RefreshRecyclerLayout refreshRecyclerLayout;

    private RecyclerView recyclerView;

    @Override
    protected void initViews() {
        AppTools.locate(this);
        fgmNearbyBinding = (FgmNearbyBinding) viewDataBinding;
        rootView = fgmNearbyBinding.getRoot();
        refreshRecyclerLayout = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R
                .layout.refresh_recycler, null, false);
        recyclerView = refreshRecyclerLayout.recyclerView;
        tvNearbyLocation = fgmNearbyBinding.tvNearbyLocation;
        listPopUpWindow = new ListPopUpWindow(getActivity());

        for (int i = 0; i < 20; i++) {
            NearbyContentBean nearbyContentBean = new NearbyContentBean();
            nearbyContentBean.setTotalPrice("50");
            nearbyContentBean.setTitle("测试标题");
            nearbyContentBean.setSubTitle("测试子标题");
            nearbyContentBean.setDetailMsg("测试下面");
            nearbyContentBean.setBounds("测试奖励豆豆");
            list.add(nearbyContentBean);
        }

        commonBinderAdapter = new CommonBinderAdapter<NearbyContentBean>(getActivity(), R.layout
                .item_nearby, list) {
            @Override
            public void onBind(ViewDataBinding viewDataBinding, CommonBinderHolder holder, int
                    position, NearbyContentBean nearbyContentBean) {
                ItemNearbyBinding itemNearbyBinding = (ItemNearbyBinding) viewDataBinding;
                itemNearbyBinding.setNearbyContent(nearbyContentBean);
            }

        };
    }

    @Override
    protected void initEvents() {
        fgmNearbyBinding.flytNearbyTopClassify.setOnClickListener(this);
        fgmNearbyBinding.flytNearbyTopNearby.setOnClickListener(this);
        fgmNearbyBinding.flytNearbyTopPrice.setOnClickListener(this);
        fgmNearbyBinding.flytNearbyTopSort.setOnClickListener(this);
        fgmNearbyBinding.ivNearbyRefresh.setOnClickListener(this);

        recyclerView.setAdapter(commonBinderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(android.R.color.transparent).sizeResId(R.dimen
                        .large_margin_15dp)
                .build());

    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.flyt_nearby_top_nearby:
                resetTopAll();
                addDebugData(TopSelection.NEARBY, R.array.nearby);
                setTopChosen(0);
                listPopUpWindow.showAsDropDown(view);
                break;
            case R.id.flyt_nearby_top_classify:
                resetTopAll();
                addDebugData(TopSelection.CLASSIFY, R.array.classify);
                setTopChosen(1);
                listPopUpWindow.showAsDropDown(view);
                break;
            case R.id.flyt_nearby_top_sort:
                resetTopAll();
                addDebugData(TopSelection.SORT, R.array.sort);
                setTopChosen(2);
                listPopUpWindow.showAsDropDown(view);
                break;
            case R.id.flyt_nearby_top_price:
                resetTopAll();
                addDebugData(TopSelection.PRICE, R.array.price);
                setTopChosen(3);
                listPopUpWindow.showAsDropDown(view);
                break;
            case R.id.iv_nearby_refresh:
                AppTools.showNormalSnackBar(view, getResources().getString(R.string
                        .main_locating));
                AppTools.locate(this);
                break;
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fgm_nearby;
    }

    private void addDebugData(TopSelection selection, int array) {
        if (this.topSelection == selection)
            return;
        parentList.clear();
        childList.clear();
        boolean hasChild = false;
        String[] arrays = getResources().getStringArray(array);
        for (int j = 0; j < arrays.length; j++) {
            NearbyBean bean = new NearbyBean();
            bean.setText(arrays[j]);
            bean.setDrawable(null);
            bean.setColor(R.color.color_text_dark);
            if (selection == TopSelection.CLASSIFY) {
                List<NearbyBean> list = new ArrayList<>();
                hasChild = true;
                for (int i = 0; i < 5; i++) {
                    NearbyBean nearbyBean = new NearbyBean();
                    nearbyBean.setText(arrays[j] + "" + i);
                    nearbyBean.setColor(R.color.color_text_dark);
                    nearbyBean.setDrawable(null);
                    list.add(nearbyBean);
                }
                childList.add(list);
            } else {
                hasChild = false;
            }
            parentList.add(bean);
        }
        listPopUpWindow.setItem(parentList, childList, hasChild);
        this.topSelection = selection;
    }

    //重置顶部为默认效果
    private void resetTopAll() {
        for (int id : topFlyts) {
            FrameLayout frameLayout = (FrameLayout) rootView.findViewById(id);
            TextView textView = (TextView) frameLayout.getChildAt(0);
            textView.setTextColor(getResources().getColor(R.color.color_text_dark));
            ImageView imageView = (ImageView) frameLayout.getChildAt(2);
            imageView.setImageResource(R.mipmap.ic_trangle_nor);
        }
    }

    //设置顶部选中效果
    private void setTopChosen(int index) {
        FrameLayout frameLayout = (FrameLayout) rootView.findViewById(topFlyts[index]);
        TextView textView = (TextView) frameLayout.getChildAt(0);
        textView.setTextColor(getResources().getColor(R.color.theme_primary));
        ImageView imageView = (ImageView) frameLayout.getChildAt(2);
        imageView.setImageResource(R.mipmap.ic_trangle_press);
    }

    //百度定位回调
    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null) {
            LogTools.w("bd location null");
            AppTools.locate(this);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(bdLocation.getProvince());
        stringBuilder.append(bdLocation.getCity());
        stringBuilder.append(bdLocation.getDistrict());
        stringBuilder.append(bdLocation.getStreet());
        tvNearbyLocation.setText(stringBuilder.toString());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppTools.stopLocate();
    }

    @Override
    public void onPopUpItemClick(int parentIndex, int childIndex) {

    }
}
