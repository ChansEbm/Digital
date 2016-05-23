package com.szbb.pro.model;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.szbb.pro.AppKeyMap;
import com.szbb.pro.ItemAddPicLayout;
import com.szbb.pro.PictureItem;
import com.szbb.pro.R;
import com.szbb.pro.eum.Perform;
import com.szbb.pro.impl.OnAddPictureDoneListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.BitmapCompressTool;
import com.szbb.pro.tools.FrescoTools;
import com.szbb.pro.tools.ObjectAnimatorTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 11/18/2015.
 * 添加删除照片动作实现类
 */
public class MarkPictureModel implements View.OnClickListener {
    private LinearLayout linearLayout;
    private OnAddPictureDoneListener onAddPictureDoneListener;
    private ObjectAnimatorTools tools = new ObjectAnimatorTools();
    private int tagPos = -1;

    private boolean isNeedDeleteAnimation = true;
    private boolean isNeedDeleteIcon = true;
    private boolean isNeedAuto = true;

    private List<String> pathCache = new ArrayList<>();

    private void addSinglePictureInLinearLayoutByLocal(Context context, LinearLayout linearLayout,
                                                       @NonNull String filePath) {
        if (TextUtils.isEmpty(filePath)) {//如果路径为默认值,则返回不执行任何操作
            return;
        }
        this.linearLayout = linearLayout;//初始化linearLayout
        PictureItem pictureItem = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .item_pics, null, false);//初始化picItem
        SimpleDraweeView simpleDraweeView = pictureItem.simpleDraweeView;
        ImageView deleteImageView = pictureItem.cancel;
        deleteImageView.setTag(tagPos);
        if (!isNeedDeleteIcon)
            deleteImageView.setVisibility(View.GONE);
        else
            deleteImageView.setVisibility(View.VISIBLE);

        pictureItem.cancel.setOnClickListener(this);

        pictureItem.getRoot().setTag(filePath);
        BitmapCompressTool.getRadioBitmap(filePath, 500, 500);//压缩图片宽高为500*500
        simpleDraweeView.setTag(filePath);//设置标识为图片路径
        simpleDraweeView.setImageURI(Uri.parse("file://" + filePath));//加载图片并显示

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);

        int childCount = this.linearLayout.getChildCount();//获得要添加图片的布局里面带有多少个控件
        int count = childCount > 0 ? childCount - 1 : childCount;
        for (int i = 0; i < count; i++) {
            View parentView = linearLayout.getChildAt(i);
            String tag = (String) parentView.getTag();
            if (TextUtils.equals(tag, filePath)) return;
        }
        if (childCount > 0) {//如果有控件,则证明该布局有一个+的引导,我们要把图片加到它前面
            this.linearLayout.addView(pictureItem.getRoot(), childCount - 1, params);
        } else {//否则直接加到布局里头
            this.linearLayout.addView(pictureItem.getRoot(), params);
        }

        if (onAddPictureDoneListener != null) {//添加完成后,回调,在回调处作相应处理
            onAddPictureDoneListener.onAddPictureDone(pictureItem.getRoot(), this.linearLayout
                    .getChildCount());
        }
    }

    private void addSinglePictureInLinearLayoutByNetwork(Context context, LinearLayout linearLayout,
                                                         @NonNull String link) {
        PictureItem pictureItem = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout
                .item_pics, null, false);
        pictureItem.cancel.setVisibility(View.GONE);
        pictureItem.simpleDraweeView.setImageURI(Uri.parse(link));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 0, 10, 0);
        linearLayout.addView(pictureItem.getRoot(), params);
    }

    public void savePicturePath(@NonNull String path) {
        if (!TextUtils.isEmpty(path))
            this.pathCache.add(path);
    }


    public void addSinglePictureInLinearLayout(Context context, LinearLayout linearLayout,
                                               boolean isFromNetwork) {
        if (context == null || linearLayout == null)
            return;
        if (this.pathCache.size() == 0 && isNeedAuto) {
            linearLayout.removeAllViews();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.empty_item);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AppTools.dip2px(76),
                    AppTools.dip2px(76));
            linearLayout.addView(imageView, params);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            return;
        }
        for (String s : pathCache) {
            if (isFromNetwork) {
                addSinglePictureInLinearLayoutByNetwork(context, linearLayout, s);
            } else {
                addSinglePictureInLinearLayoutByLocal(context, linearLayout, s);
            }
        }
    }


    public void removeAllPicture(LinearLayout linearLayout, int pos, View.OnClickListener
            onClickListener) {
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = (LayoutInflater) linearLayout.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemAddPicLayout itemAddPicLayout = DataBindingUtil.inflate(layoutInflater, R.layout
                .item_add_pic, null, false);
        itemAddPicLayout.btnAddPic.setOnClickListener(onClickListener);
        itemAddPicLayout.btnAddPic.setTag(pos);
        linearLayout.addView(itemAddPicLayout.getRoot());
    }

    public void setOnAddPictureDoneListener(OnAddPictureDoneListener onAddPictureDoneListener) {
        this.onAddPictureDoneListener = onAddPictureDoneListener;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {//删除图片
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                if (linearLayout.getChildAt(i).findViewById(R.id.cancel) == v) {//如果符合点击项
                    View parentView = linearLayout.getChildAt(i);//获得要删除的对象
                    if (isNeedDeleteAnimation) {
                        removePicture(parentView, i);//进行动画删除
                        removeDeletedPath(parentView);
                        break;
                    } else {
                        if (onAddPictureDoneListener != null)
                            onAddPictureDoneListener.onDeletePictureDone(parentView, linearLayout
                                    .getChildCount(), i, tagPos);
                        this.linearLayout.removeView(parentView);
                        removeDeletedPath(parentView);
                        break;
                    }
                }
            }
        }
    }

    private void removeDeletedPath(View parentView) {
        String path = (String) parentView.findViewById(R.id.simpleDraweeView).getTag();
        if (pathCache.contains(path)) {
            pathCache.remove(path);
        }
    }


    /**
     * 动画删除项
     *
     * @param parentView 要删除并且进行动画的项
     */
    private void removePicture(final View parentView, final int position) {
        if (tools.isAnimationRunning()) {//如果动画正在进行中,,则直接返回
            return;
        }
        //属性动画,对宽做动画,从原本宽度到0
        tools.performAnimate(parentView, Perform.WIDTH, AppKeyMap.DEFAULT_DURATION, parentView
                .getWidth(), 0);
        //同时做渐变动画 200ms内从1f变成0.3f
        ViewPropertyAnimator.animate(parentView).alpha(0.3f).setDuration(200).start();
        //延迟x ms后执行删除成功的回调
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (onAddPictureDoneListener != null)
                    onAddPictureDoneListener.onDeletePictureDone(parentView, linearLayout
                            .getChildCount(), position, tagPos);
                linearLayout.removeView(parentView);

            }
        }, AppKeyMap.DEFAULT_DURATION);
    }

    public void deletePictureByTag(String tag, LinearLayout linearLayout) {
        deletePictureByTagWithOffset(tag, -1, linearLayout);
    }

    public void deletePictureByTagWithOffset(String tag, int pos, LinearLayout linearLayout) {
        deletePictureByTagWithInterval(tag, pos, linearLayout.getChildCount() - 1, linearLayout);
    }

    public void deletePictureByTagWithInterval(@NonNull String tag, int startPos, int endPos,
                                               LinearLayout linearLayout) {
        for (int i = startPos; i < endPos; i++) {
            View child = linearLayout.getChildAt(i);
            String childTag = (String) child.getTag();
            if (!TextUtils.isEmpty(childTag)) {
                if (TextUtils.equals(childTag, tag)) {
                    linearLayout.removeViewAt(i);
                    return;
                }
            }
        }
    }


    public void setIsNeedDeleteAnimation(boolean isNeedDeleteAnimation) {
        this.isNeedDeleteAnimation = isNeedDeleteAnimation;
    }

    public void setIsNeedDeleteIcon(boolean isNeedDeleteIcon) {
        this.isNeedDeleteIcon = isNeedDeleteIcon;
    }

    public void setAutoAddPics(boolean isNeedAuto) {
        this.isNeedAuto = isNeedAuto;
    }

    public void setTagPos(int tagPos) {
        this.tagPos = tagPos;
    }
}
