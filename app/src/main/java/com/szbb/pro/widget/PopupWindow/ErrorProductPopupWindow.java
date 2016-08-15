package com.szbb.pro.widget.PopupWindow;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.PopupErrorProductLayout;
import com.szbb.pro.R;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnErrorProductCallback;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.model.MarkPictureModel;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.widget.deleter.DeleterHandlerCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;


/**
 * Created by ChanZeeBm on 2016/1/22.
 */
public class ErrorProductPopupWindow extends BasePopupWindow implements DeleterHandlerCallback {
    private OnErrorProductCallback onErrorProductCallback;
    private ArrayList<String> filePaths = new ArrayList<>();
    private String detailId = "";
    private String info = "";

    private EditText edtError;

    public ErrorProductPopupWindow(Context context) {
        super(context);
        PopupErrorProductLayout popupErrorProductLayout = (PopupErrorProductLayout) viewDataBinding;
        edtError = popupErrorProductLayout.edtError;
        popupErrorProductLayout.cancel.setOnClickListener(this);
        popupErrorProductLayout.submit.setOnClickListener(this);
        popupErrorProductLayout.deleterScrollLayout.setDeleterHandlerCallback(this);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setFocusable(true);
    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_error_product;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                dismiss();
                break;
            case R.id.submit:
                if (checkInfo()) {
                    if (onErrorProductCallback != null) {
                        onErrorProductCallback.onSubmit(detailId, info, filePaths);
                    }
                    dismiss();
                }
                break;

        }
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public void setOnErrorProductCallback(OnErrorProductCallback onErrorProductCallback) {
        this.onErrorProductCallback = onErrorProductCallback;
    }

    private boolean checkInfo() {
        info = edtError.getText().toString();
        if (info.isEmpty()) {
            AppTools.showNormalSnackBar(appCompatActivity.getWindow().getDecorView(), "请输入正确产品信息"
            );
            return false;
        }
        if (filePaths.isEmpty()) {
            AppTools.showNormalSnackBar(appCompatActivity.getWindow().getDecorView(), "请上传至少一张图片信息"
            );
            return false;
        }
        return true;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (!detailId.isEmpty()) {
            super.showAtLocation(parent, gravity, x, y);
            AppTools.setWindowBackground(appCompatActivity, 1.0f);
        }
    }


    @Override
    public void success(Set<Integer> keySet, List<String> photoPaths) {
        this.filePaths.clear();
        this.filePaths.addAll(photoPaths);
    }
}
