package com.szbb.pro.model.order_detail;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.szbb.pro.AppKeyMap;
import com.szbb.pro.R;
import com.szbb.pro.dialog.MessageDialog;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.LogTools;


/**
 * Created by KenChan on 16/6/13.
 */
public class LabelManager implements View.OnClickListener {
    private MessageDialog messageDialog;
    private AppCompatActivity appCompatActivity;

    public LabelManager(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
    }

    public void addLabel(@NonNull LinearLayout linearLayout, @NonNull String label) {
        Context context = linearLayout.getContext();
        if (label.isEmpty())
            return;

        String[] labels = label.split(",");

        linearLayout.removeAllViews();
        if (labels.length == 1) {
            TextView textView = getLabelTextView(context);
            textView.setText(label);
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.CUPCAKE);
            linearLayout.addView(textView, 0);
            return;
        }
        int count = labels.length > 3 ? 3 : labels.length;
        for (int i = 0; i < count; i++) {
            TextView textView = getLabelTextView(context);
            textView.setText(labels[i]);
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.CUPCAKE);
            linearLayout.addView(textView, i);
        }
        if (labels.length > 3) {
            TextView textView = getLabelTextView(context);
            textView.setText("...");
            textView.setOnClickListener(this);
            textView.setTag(R.id.tag_cupcake, AppKeyMap.DONUT);
            textView.setTag(R.id.tag_donut, labels);
            linearLayout.addView(textView, linearLayout.getChildCount());
        }
    }

    private TextView getLabelTextView(Context context) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(AppTools.dip2px
                (56), AppTools.dip2px(22));
        //sdk版本必须大于等于API17才有setMarginEnd方法
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginEnd(AppTools.dip2px((int) context.getResources().getDimension(R
                    .dimen.small_margin_5dp)));
        } else {
            layoutParams.rightMargin = AppTools.dip2px((int) context.getResources().getDimension(R
                    .dimen.small_margin_5dp));
        }

        TextView textView = new TextView(context);
        textView.setLayoutParams(layoutParams);
        textView.setBackgroundResource(R.drawable.bg_orange_frame);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(3, 0, 3, 0);
        textView.setSingleLine(true);
        textView.setMaxEms(4);
        textView.setTextColor(context.getResources().getColor(R.color.color_orange_light));
        textView.setEllipsize(TextUtils.TruncateAt.MIDDLE);

        return textView;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        initMessageDialog();
        int flag = (int) v.getTag(R.id.tag_cupcake);
        TextView textView = (TextView) v;
        if (flag == AppKeyMap.CUPCAKE) {//not more label
            messageDialog.setMessage(textView.getText().toString());
        } else if (flag == AppKeyMap.DONUT) {//more label
            StringBuilder stringBuilder = new StringBuilder();
            String[] labels = (String[]) v.getTag(R.id.tag_donut);
            for (int i = 3; i < labels.length; i++) {
                stringBuilder.append(labels[i]);
                stringBuilder.append(",");
                LogTools.i(stringBuilder.toString());
            }
            messageDialog.setMessage(stringBuilder.toString());
        }
        messageDialog.show();
    }

    private void initMessageDialog() {

        messageDialog = new MessageDialog(appCompatActivity);
        messageDialog.setTitle(appCompatActivity.getString(R.string.label_malfunction_status));
    }


}
