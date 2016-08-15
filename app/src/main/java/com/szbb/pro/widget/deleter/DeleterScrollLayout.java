package com.szbb.pro.widget.deleter;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.HorizontalScrollView;

import com.szbb.pro.R;

/**
 * Created by KenChan on 16/5/26.
 */
public class DeleterScrollLayout
        extends HorizontalScrollView {

    private DeleterLayout deleterLayout;
    private DeleterConfigsBuilder builder = new DeleterConfigsBuilder();

    public DeleterScrollLayout(Context context) {
        super(context);
    }

    public DeleterScrollLayout(Context context,
                               AttributeSet attrs) {
        super(context,
              attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                                                      R.styleable.DeleterScrollLayout);
        int indexCount = a.getIndexCount();
        for (int i = 0;
             i < indexCount;
             i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.DeleterScrollLayout_deleter_deleteBitmapRes:
                    builder.setDeleteBitmapRes(a.getResourceId(attr,
                                                               R.mipmap.delete));
                    break;
                case R.styleable.DeleterScrollLayout_deleter_adderBitmapRes:
                    builder.setAdderRes(a.getResourceId(attr,
                                                        R.mipmap.ic_launcher));
                    break;
                case R.styleable.DeleterScrollLayout_deleter_maxLimit:
                    builder.setMaxPhotoLimit(a.getInteger(attr,
                                                          5));
                    break;
                case R.styleable.DeleterScrollLayout_deleter_height:
                    int heightDimension = a.getDimensionPixelSize(attr,
                                                                  (int) TypedValue.applyDimension
                                                                          (TypedValue
                                                                                   .COMPLEX_UNIT_DIP,
                                                                           100f,
                                                                           getResources()
                                                                                   .getDisplayMetrics()));
                    builder.setDeleterHeight(heightDimension);
                    break;
                case R.styleable.DeleterScrollLayout_deleter_width:
                    int widthDimension = a.getDimensionPixelSize(attr,
                                                                 (int) TypedValue.applyDimension
                                                                         (TypedValue
                                                                                  .COMPLEX_UNIT_DIP,
                                                                          100f,
                                                                          getResources()
                                                                                  .getDisplayMetrics()));
                    builder.setDeleterWidth(widthDimension);
                    break;
                case R.styleable.DeleterScrollLayout_deleter_hasAdderIcon:
                    boolean hasAdderIcon = a.getBoolean(attr,
                                                        false);
                    builder.setHasAdderIcon(hasAdderIcon);
                    break;
                case R.styleable.DeleterScrollLayout_deleter_margin:
                    int marginDimension = a.getDimensionPixelSize(attr,
                                                                  (int) TypedValue.applyDimension
                                                                          (TypedValue
                                                                                   .COMPLEX_UNIT_DIP,
                                                                           10f,
                                                                           getResources()
                                                                                   .getDisplayMetrics()));
                    builder.setMargin(marginDimension);
                    break;
            }
        }
        a.recycle();
        deleterLayout = new DeleterLayout(context,
                                          attrs,
                                          builder.createDeleterConfigs());
        addView(deleterLayout);

    }

    public DeleterScrollLayout(Context context,
                               AttributeSet attrs,
                               int defStyleAttr) {
        super(context,
              attrs,
              defStyleAttr);
        deleterLayout = new DeleterLayout(context,
                                          attrs,
                                          defStyleAttr);
        addView(deleterLayout);
    }

    public void setConfigs(DeleterConfigs configs) {
        deleterLayout.setConfigs(configs);
    }

    public DeleterConfigsBuilder getConfigsBuilder() {
        return builder;
    }

    public void setPlacerImages(@DrawableRes int... placerImages) {
        deleterLayout.setPlacerImages(placerImages);
    }

    public void setDeleterHandlerCallback(DeleterHandlerCallback deleterHandlerCallback) {
        deleterLayout.setDeleterHandlerCallback(deleterHandlerCallback);
    }
}
