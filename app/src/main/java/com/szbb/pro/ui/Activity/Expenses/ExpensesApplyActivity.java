package com.szbb.pro.ui.activity.expenses;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ExpensesApplyLayout;
import com.szbb.pro.R;
import com.szbb.pro.base.BaseAty;
import com.szbb.pro.dialog.InputDialog;
import com.szbb.pro.entity.base.BaseBean;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.eum.WheelOptions;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.impl.OnWheelOptsSelectCallback;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.CameraTools;
import com.szbb.pro.tools.LogTools;
import com.szbb.pro.ui.activity.main.MainActivity;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;
import com.szbb.pro.widget.PopupWindow.WheelPopupWindow;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;

/**
 * 费用申请
 */
public class ExpensesApplyActivity
        extends BaseAty<BaseBean, BaseBean>
        implements
        OnWheelOptsSelectCallback, InputCallBack, DeleterHandlerCallback {
    private ExpensesApplyLayout expensesApplyLayout;
    private EditText editText;
    private TextView tvType;
    private TextView tvExpenses;

    private ArrayList<String> alreadyAddPics = new ArrayList<>();
    private WheelPopupWindow wheelPopupWindow;

    private String costType = "";
    private String money = "";
    private String remarks = "";

    private String detailId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        expensesApplyLayout = (ExpensesApplyLayout) viewDataBinding;
        if (getIntent() != null) {
            detailId = getIntent().getStringExtra("detailid");
        } else {
            AppTools.removeSingleActivity(this);
        }
    }

    @Override
    protected void initViews() {
        defaultTitleBar(this).setTitle(R.string.title_expenses_apply);
        tvType = expensesApplyLayout.tvType;
        tvExpenses = expensesApplyLayout.tvExpenses;
        editText = expensesApplyLayout.editText;
        wheelPopupWindow = new WheelPopupWindow(this);
    }

    @Override
    protected void initEvents() {
        expensesApplyLayout.ryltMoney.setOnClickListener(this);
        expensesApplyLayout.ryltType.setOnClickListener(this);
        expensesApplyLayout.btnSubmit.setOnClickListener(this);
        expensesApplyLayout.expensesDeleterScrollLayout.setDeleterHandlerCallback(this);

    }

    @Override
    protected void noNetworkStatus() {

    }

    @Override
    protected int getContentView() {
        return R.layout.aty_expenses_apply;
    }

    @Override
    protected void onClick(int id, View view) {
        switch (id) {
            case R.id.rylt_type:
                wheelPopupWindow.setOptions(WheelOptions.STANDER);
                wheelPopupWindow.setPopupTitle(getString(R.string.expenses_apply_type));
                wheelPopupWindow.setCurvedData(getResources().getStringArray(R.array
                                                                                     .expenses_apply));
                wheelPopupWindow.setOnWheelOptsSelectCallback(this);
                wheelPopupWindow.showAtLocation(parentView,
                                                Gravity.BOTTOM,
                                                0,
                                                0);
                break;
            case R.id.rylt_money:
                InputDialog inputDialog = new InputDialog(this,
                                                          true);
                inputDialog.setParams(NetworkParams.DONUT);
                inputDialog.setTitle(getString(R.string.expenses_apply_price));
                inputDialog.setInputCallBack(this);
                inputDialog.show();
                break;

            case R.id.btn_submit:
                if (checkInfo()) {
                    networkModel.applyCost(detailId,
                                           money,
                                           costType,
                                           remarks,
                                           alreadyAddPics,
                                           NetworkParams.CUPCAKE);
                }
                break;
            case R.id.frameLayout:
                AppTools.hideSoftInputMethod(view);
                break;
        }
    }

    private boolean checkInfo() {
        remarks = editText.getText()
                          .toString();
        if (TextUtils.isEmpty(costType)) {
            AppTools.showNormalSnackBar(parentView,
                                        getString(R.string
                                                          .expenses_please_choose_type));
            return false;
        }
        if (TextUtils.isEmpty(money)) {
            AppTools.showNormalSnackBar(parentView,
                                        getString(R.string
                                                          .expenses_please_input_money));
            return false;
        }
        return true;
    }


    @Override
    public void onJsonObjectSuccess(BaseBean baseBean, NetworkParams paramsCode) {
        start(MainActivity.class,
              Intent.FLAG_ACTIVITY_CLEAR_TOP,
              Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    @Override
    public void onWheelSelect(String selectData, WheelOptions wheelOptions, int index) {
        tvType.setText(selectData);
        costType = (index + 1) + "";
    }

    @Override
    public void inputWord(String word, NetworkParams networkParams) {
        switch (networkParams) {
            case DONUT:
                money = word;
                tvExpenses.setText("¥" + money + "元");
                break;
        }
    }

    @Override
    public void success(Set<Integer> keySet, List<String> photoPaths) {
        alreadyAddPics.clear();
        alreadyAddPics.addAll(photoPaths);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == AppKeyMap
                .MARSHMALLOW) {

        }
    }
}
