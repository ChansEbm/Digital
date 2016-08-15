package com.szbb.pro.widget.PopupWindow;

import android.Manifest;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.databinding.PopupTakePhotoBinding;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.tools.PermissionTools;

/**
 * Created by ChanZeeBm on 2015/11/18.
 */
public class PhotoPopupWindow
        extends BasePopupWindow {
    private PopupTakePhotoBinding popupTakePhotoBinding;
    private OnPhotoOptsSelectListener onPhotoOptsSelectListener;

    public PhotoPopupWindow(@NonNull Context context) {
        super(context);
        popupTakePhotoBinding = (PopupTakePhotoBinding) viewDataBinding;
        popupTakePhotoBinding.btnCancel.setOnClickListener(this);
        popupTakePhotoBinding.btnSelectFromAlbum.setOnClickListener(this);
        popupTakePhotoBinding.btnTakePhoto.setOnClickListener(this);
    }

    @Override
    public int getPopupLayout() {
        return R.layout.popup_take_photo;
    }

    public void setOnPhotoOptsSelectListener(OnPhotoOptsSelectListener onPhotoOptsSelectListener) {
        this.onPhotoOptsSelectListener = onPhotoOptsSelectListener;
    }

    @Override
    public void onClick(View v) {
        if (onPhotoOptsSelectListener != null) {
            switch (v.getId()) {
                case R.id.btn_select_from_album:
                    if (PermissionTools.alreadyHasPermission(appCompatActivity,
                                                             AppKeyMap.LOLLIPOP,
                                                             Manifest.permission
                                                                     .READ_EXTERNAL_STORAGE)) {
                        onPhotoOptsSelectListener.onOptsSelect(PhotoPopupOpts.ALBUM);
                    }
                    break;
                case R.id.btn_take_photo:
                    if (PermissionTools.alreadyHasPermission(appCompatActivity,
                                                             AppKeyMap.MARSHMALLOW,
                                                             Manifest.permission
                                                                     .CAMERA)) {
                        onPhotoOptsSelectListener.onOptsSelect(PhotoPopupOpts.TAKE_PHOTO);
                    }
                    break;
            }
        }
        dismiss();
    }

    public void setHideTakePhotoButton() {
        popupTakePhotoBinding.btnTakePhoto.setVisibility(View.GONE);
    }



}
