package com.szbb.pro.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.szbb.pro.R;
import com.szbb.pro.eum.NetworkParams;
import com.szbb.pro.impl.InputCallBack;
import com.szbb.pro.tools.LogTools;

import java.lang.reflect.Field;

import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by ChanZeeBm on 2015/12/29.
 */
public class InputDialog implements View.OnClickListener {
    private InputCallBack inputCallBack;
    private MaterialDialog materialDialog;
    private EditText editText;
    private String title;//标题
    private NetworkParams params;//标记
    private AlertDialog alertDialog;

    public InputDialog (Context context) {
        this(context, false);
    }

    public InputDialog (final Context context, boolean isCitizenId) {
        editText = new EditText(context);
        editText.setSingleLine(true);
        setEditTextInputType(isCitizenId);
        materialDialog = new MaterialDialog(context).setTitle(title)
                                                    .setContentView(editText)
                                                    .setPositiveButton(R.string.positive,
                                                                       new View.OnClickListener() {

                                                                           @Override
                                                                           public void onClick (
                                                                                   View v) {
                                                                               String word =
                                                                                       editText
                                                                                               .getText()
                                                                                               .toString();
                                                                               if (inputCallBack !=
                                                                                   null &&
                                                                                   !word.isEmpty
                                                                                           ()) {
                                                                                   inputCallBack
                                                                                           .inputWord(
                                                                                                   word,
                                                                                                   params);
                                                                               }
                                                                               materialDialog
                                                                                       .dismiss();
                                                                           }
                                                                       })
                                                    .setNegativeButton(R.string.negative,
                                                                       new View.OnClickListener() {
                                                                           @Override
                                                                           public void onClick (
                                                                                   View v) {
                                                                               materialDialog
                                                                                       .dismiss();
                                                                           }
                                                                       })
                                                    .setCanceledOnTouchOutside(true);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange (View v, boolean hasFocus) {
                if (hasFocus) {
                    if (alertDialog != null) {
                        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                                                                         .SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    }
                }
            }
        });
    }

    private void setEditTextInputType (boolean isCitizenId) {
        if (isCitizenId) {
            String digital = "1234657890xX";
            editText.setKeyListener(DigitsKeyListener.getInstance(digital));
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        }
    }

    private void initAlterDialog () {
        try {
            Field f = materialDialog.getClass().getDeclaredField("mAlertDialog");
            f.setAccessible(true);
            alertDialog = (AlertDialog) f.get(materialDialog);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setInputType (int inputType) {
        setEditTextInputType(inputType, false);
    }

    public void setEditTextInputType (int inputType, boolean isPassword) {
        if (!isPassword) {
            editText.setKeyListener(DigitsKeyListener.getInstance());
            editText.setInputType(inputType);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void setInputCallBack (InputCallBack inputCallBack) {
        this.inputCallBack = inputCallBack;
    }

    public void setTitle (@NonNull String title) {
        this.title = title;
        materialDialog.setTitle(title);
    }

    public void setParams (@NonNull NetworkParams params) {
        this.params = params;
    }

    public void show () {
        show(null);
    }

    public void show (String msg) {

        if (TextUtils.isEmpty(title)) {
            LogTools.e("title not set");
            return;
        }
        editText.setText(TextUtils.isEmpty(msg) ? "" : msg);
        materialDialog.show();
        initAlterDialog();
        editText.requestFocus();
        editText.setFocusable(true);
    }

    @Override
    public void onClick (View v) {

    }
}
