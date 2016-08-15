package com.szbb.pro.widget.deleter;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.BitmapCompressTool;
import com.szbb.pro.tools.ScreenTools;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by KenChan on 16/5/26.
 */
public class DeleterLayout
        extends ViewGroup
        implements DeleterLayoutCallback {

    private HashMap<Integer, String> savePhotoPaths = new HashMap<>();

    private DeleterHandlerCallback deleterHandlerCallback;

    private int placerCount = 0;
    private int[] placerImages = new int[]{};
    private DeleterConfigs configs;

    public DeleterLayout(Context context,
                         DeleterConfigs configs) {
        super(context);
        this.configs = configs;
    }

    public DeleterLayout(Context context,
                         AttributeSet attrs,
                         DeleterConfigs configs) {
        super(context,
              attrs);
        this.configs = configs;
    }

    public DeleterLayout(Context context,
                         AttributeSet attrs,
                         int defStyleAttr) {
        super(context,
              attrs,
              defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        if (getChildCount() == 0) {
            addPhotoIntoLayoutSelf();
        }
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int pMeasureWidth = 0;
        int pMeasureHeight = 0;

        int childCount = getChildCount();
        for (int i = 0;
             i < childCount;
             i++) {
            View childView = getChildAt(i);
            measureChild(childView,
                         widthMeasureSpec,
                         heightMeasureSpec);
            int childViewMeasuredWidth = childView.getMeasuredWidth();
            int childViewMeasuredHeight = childView.getMeasuredHeight();

            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            int leftMargin = params.leftMargin;
            int topMargin = params.topMargin;
            int bottomMargin = params.bottomMargin;

            int currentChildWidth = leftMargin + childViewMeasuredWidth;
            int currentChildHeight = topMargin + bottomMargin + childViewMeasuredHeight;
            if (childView.getVisibility() != View.GONE) {
                pMeasureWidth += currentChildWidth;
                pMeasureHeight = Math.max(pMeasureHeight,
                                          currentChildHeight);
            }
        }
        if (pMeasureWidth < ScreenTools.getScreenWidth(getContext())) {
            pMeasureWidth = widthMode == MeasureSpec.EXACTLY ? widthSize : pMeasureWidth +
                    configs.margin;
        } else {
            pMeasureWidth += configs.margin;
        }
        pMeasureHeight = heightMode == MeasureSpec.EXACTLY ? heightSize : pMeasureHeight + 2 *
                configs.margin;

        setMeasuredDimension(pMeasureWidth,
                             pMeasureHeight);

    }

    @Override
    protected void onLayout(boolean changed,
                            int l,
                            int t,
                            int r,
                            int b) {
        //放置子view并且全部垂直居中
        int childCount = getChildCount();
        int left = getFirstLayoutLeftMargin();
        for (int i = 0;
             i < childCount;
             i++) {
            View childView = getChildAt(i);
            int childViewMeasuredWidth = childView.getMeasuredWidth();
            int childViewMeasuredHeight = childView.getMeasuredHeight();

            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;

            int halfMeasuredHeight = getMeasuredHeight() / 2;

            int cl = left;
            int cr = cl + childViewMeasuredWidth;
            int ct = halfMeasuredHeight - childViewMeasuredHeight / 2;
            int cb = ct + childViewMeasuredHeight;
            childView.layout(cl,
                             ct,
                             cr,
                             cb);
            left += childViewMeasuredWidth + leftMargin;
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),
                                      attrs);
    }

    private void addPhotoIntoLayoutSelf() {
        if (configs.getMode() == DeleterConfigs.MODE_HAND) {
            Log.e("digital",
                  "addPhotoIntoLayoutSelf: hand"
                 );
            if (placerCount != 0) {
                configs.surplusSelectLimit = configs.maxPhotoLimit - placerCount;
                for (int i = 0;
                     i < placerCount;
                     i++) {
                    DeleterImageView imageView = getAdderDeleterImageView();
                    imageView.setImageResource(placerImages[i]);
                    imageView.setRole(DeleterImageView.PLACER);
                    savePhotoPaths.put(i,
                                       "");
                    addView(imageView,
                            i,
                            imageView.getLayoutParams());
                }
            }
            if (configs.hasAdderIcon) {
                DeleterImageView imageView = getAdderDeleterImageView();
                imageView.setImageResource(configs.adderRes);
                imageView.setRole(DeleterImageView.ADDER);
                addView(imageView,
                        getChildCount(),
                        imageView.getLayoutParams());
            }
        } else if (configs.getMode() == DeleterConfigs.MODE_VIEW && configs.getNetworkUrls() !=
                null) {
            removeAllViews();
            Log.i("digital",
                  "addPhotoIntoLayoutSelf: " + configs.getNetworkUrls()
                                                      .isEmpty());
            for (String url : configs.getNetworkUrls()) {
                if (TextUtils.isEmpty(url)) {
                    continue;
                }
                DeleterImageView imageView = getAdderDeleterImageView();
                imageView.setRole(DeleterImageView.VIEWER);
                Picasso.with(getContext())
                       .load(url)
                       .resize(500,
                               500)
                       .into(imageView);
                addView(imageView,
                        getChildCount(),
                        imageView.getLayoutParams());
            }
        }
    }

    private DeleterImageView getAdderDeleterImageView() {
        DeleterImageView imageView = new DeleterImageView(getContext(),
                                                          configs);
        MarginLayoutParams params = new MarginLayoutParams(configs.deleterWidth,
                                                           configs.deleterHeight);
        params.setMargins(configs.margin,
                          configs.margin,
                          configs.margin,
                          configs.margin);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(params);
        imageView.setFlag(getDeleterImageViewFlag());
        imageView.initDeleterLayoutCallback(this);
        return imageView;
    }

    private int getFirstLayoutLeftMargin() {
        View childView = getChildAt(0);
        if (childView != null) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childView
                    .getLayoutParams();
            return marginLayoutParams.leftMargin;
        }
        return 0;
    }


    @Override
    public void photoAdd(String photoPath,
                         DeleterImageView deleterImageView) {
        //判断是否超过最大数量
        if (getChildCount() - 1 == configs.maxPhotoLimit) {
            Toast.makeText(DeleterLayout.this.getContext(),
                           "超过图片最大数量值",
                           Toast.LENGTH_LONG)
                 .show();
            return;
        }
        for (int i = 0;
             i < getChildCount();
             i++) {//遍历整个ViewGroup
            if (getChildAt(i).equals(deleterImageView)) {//找到添加占位符所在位置
                DeleterImageView imageView = getAdderDeleterImageView();
                imageView.setRole(DeleterImageView.CHILDREN);//设置角色
                imageView.setPhotoPath(photoPath);
                String osPath = compressPhoto(photoPath);
//                Picasso.with(getContext()).load(new File(photoPath)).resize(150, 150).into
// (imageView);
                imageView.setImageURI(Uri.fromFile(new File(photoPath)));
                addView(imageView,
                        i);//添加到ViewGroup
                savePhotoPaths.put(imageView.getFlag(),
                                   osPath);//把路径保存起来,用于回调
                break;
            }
        }
        //如果达到最大值,则隐藏添加图片标识
        if (configs.hasAdderIcon) {
            if (getChildCount() - 1 == configs.maxPhotoLimit) {
                deleterImageView.setVisibility(View.GONE);
            } else {
                deleterImageView.setVisibility(View.VISIBLE);
            }
        }
        callBackSuccess();
    }

    @Override
    public void placerAdd(String photoPath,
                          DeleterImageView deleterImageView) {
        for (int i = 0;
             i < getChildCount();
             i++) {
            //当改变了占位图的图片的时候,保存或者替换原来的图片路径
            if (getChildAt(i).equals(deleterImageView)) {
                String osPath = compressPhoto(photoPath);
                savePhotoPaths.put(deleterImageView.getFlag(),
                                   osPath);
                break;
            }
        }
        callBackSuccess();
    }

    @Override
    public void photoDelete(String photoPath,
                            DeleterImageView deleterImageView) {
        int childCount = getChildCount();
        for (int i = 0;
             i < childCount;
             i++) {
            //删除图片的同时把缓存起来的路径也一起删除掉
            if (getChildAt(i).equals(deleterImageView)) {
                removeViewAt(i);
                savePhotoPaths.remove(deleterImageView.getFlag());
                break;
            }
        }
        childCount = getChildCount();
        //删除了图片后判断是否有添加图片标识符并且是否达到再次显示的条件,达到了就再显示添加图片出来
        if (childCount - 1 < configs.maxPhotoLimit && configs.hasAdderIcon) {
            getChildAt(getChildCount() - 1).setVisibility(View.VISIBLE);
        }
        callBackSuccess();
        postInvalidate();
    }

    private void callBackSuccess() {
        Set<Integer> keySet = savePhotoPaths.keySet();//所有图片的key值
        List<String> photo = new ArrayList<>();//所有图片的路径
        configs.alreadyAddPhoto.clear();//清空过滤图片list

        for (Integer key : keySet) {//遍历所有已添加的图片的key跟value,并且保存起来用于回调给用户调用
            String photoPath = savePhotoPaths.get(key);
            if (!TextUtils.isEmpty(photoPath)) {
                configs.alreadyAddPhoto.add(photoPath);
                photo.add(photoPath);
            }
        }
        if (deleterHandlerCallback != null) {
            deleterHandlerCallback.success(keySet,
                                           photo);//添加成功后的回调
        }
        calcSelectLimit();
    }

    public int getDeleterImageViewFlag() {
        //获得100----9999的随机数 用于作为保存图片路径的key
        Random random = new Random();
        int flag = random.nextInt(9999) % (9999 - 100 + 1) + 100;
        for (int i = 0;
             i < savePhotoPaths.keySet()
                               .size();
             i++) {
            if (savePhotoPaths.keySet()
                              .contains(flag)) {
                i = 0;
                flag = random.nextInt(9999) % (9999 - 100 + 1) + 100;
            }
        }
        return flag;
    }

    private String compressPhoto(String photoPath) {
        if (TextUtils.isEmpty(photoPath)) {
            return "";
        }
        if (AppTools.isFileOverLimitSize(photoPath,
                                         150)) {
            return BitmapCompressTool.getRadioBitmapPath(photoPath,
                                                         800,
                                                         800);
        } else {
            return photoPath;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    public void setPlacerImages(int... placeImages) {
        //设置占位图跟占位图数量
        this.placerImages = placeImages;
        this.placerCount = this.placerImages.length;
        calcSelectLimit();
    }

    //计算可添加的图片数量
    private void calcSelectLimit() {
        int count = getChildCount() - 1 - placerCount;//计算剩下可添加图片的数量,由于添加图片的标识符不能算在内,故要-1出来
        configs.surplusSelectLimit = configs.maxPhotoLimit - count;//重置可添加图片数量
    }

    public DeleterLayout setDeleterHandlerCallback(DeleterHandlerCallback deleterHandlerCallback) {
        this.deleterHandlerCallback = deleterHandlerCallback;
        return this;
    }

    public void setConfigs(DeleterConfigs configs) {
        this.configs = configs;
        postInvalidate();
    }
}
