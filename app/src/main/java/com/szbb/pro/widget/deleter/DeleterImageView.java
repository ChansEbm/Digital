package com.szbb.pro.widget.deleter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.szbb.pro.R;
import com.szbb.pro.eum.PhotoPopupOpts;
import com.szbb.pro.impl.OnPhotoOptsSelectListener;
import com.szbb.pro.tools.AppTools;
import com.szbb.pro.tools.BitmapCompressTool;
import com.szbb.pro.widget.PopupWindow.PhotoPopupWindow;

import java.io.File;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by KenChan on 16/5/26.
 */
public class DeleterImageView
        extends ImageView {
    private Bitmap deleteBitmap;
    private Paint paint = new Paint();
    private static final String TAG = "deleterImageView";

    private int flag = 0;

    protected static final int ADDER = 0x001;
    protected static final int CHILDREN = 0x002;
    protected static final int PLACER = 0x003;
    protected static final int VIEWER = 0x004;

    private String photoPath = "";

    private int role = CHILDREN;
    private DeleterLayoutCallback deleterLayoutCallback;
    private GestureDetector gestureDetector;
    private DeleterConfigs configs = new DeleterConfigs();

    public DeleterImageView(Context context,
                            DeleterConfigs configs) {
        super(context);
        this.configs = configs;
        deleteBitmap = BitmapFactory.decodeResource(getResources(),
                                                    configs.deleteBitmapRes);
        setImageResource(R.mipmap.ic_launcher);
        paint.setAntiAlias(true);
        gestureDetector = new GestureDetector(context,
                                              new DeleterGestureDetector());
    }

    public DeleterImageView(Context context,
                            AttributeSet attrs) {
        super(context,
              attrs);
        deleteBitmap = BitmapFactory.decodeResource(getResources(),
                                                    configs.deleteBitmapRes);
        setImageResource(R.mipmap.ic_launcher);
        paint.setAntiAlias(true);
        gestureDetector = new GestureDetector(context,
                                              new DeleterGestureDetector());

    }

    public DeleterImageView(Context context,
                            AttributeSet attrs,
                            int defStyleAttr) {
        super(context,
              attrs,
              defStyleAttr);
        deleteBitmap = BitmapFactory.decodeResource(getResources(),
                                                    configs.deleteBitmapRes);
        setImageResource(R.mipmap.ic_launcher);
        paint.setAntiAlias(true);
        gestureDetector = new GestureDetector(context,
                                              new DeleterGestureDetector());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (role == CHILDREN) {//占位图跟添加标识图不能添加删除按钮
            int left = getWidth() - deleteBitmap.getWidth();
            canvas.drawBitmap(deleteBitmap,
                              left,
                              0,
                              paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public void initDeleterLayoutCallback(DeleterLayoutCallback deleterLayoutCallback) {
        this.deleterLayoutCallback = deleterLayoutCallback;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setConfigs(DeleterConfigs configs) {
        this.configs = configs;
        postInvalidate();
    }


    class DeleterGestureDetector
            extends GestureDetector.SimpleOnGestureListener
            implements GalleryFinal.OnHanlderResultCallback, OnPhotoOptsSelectListener,
            CameraCallback {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.d(TAG,
                  "onDown: ");
            if (role == VIEWER) {
                return false;
            } else if (role == PLACER) {
                return true;
            } else if (role == CHILDREN) {
                //如果标记为可编辑,则要点击删除标识才可以响应事件,否则不拦截事件
                return isInSpecialRange(e);
            } else {
                return role == ADDER;
            }
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (role == PLACER || role == ADDER) {
                //打开对话框选择图片
                PhotoPopupWindow photoPopupWindow = new PhotoPopupWindow(getContext());
                photoPopupWindow.setOnPhotoOptsSelectListener(this);
                photoPopupWindow.showAtDefaultLocation();
            } else if (role == CHILDREN) {
                //如果是可编辑,则只有可能是响应删除动作
                if (deleterLayoutCallback != null) {
                    deleterLayoutCallback.photoDelete(photoPath,
                                                      DeleterImageView.this);
                }
            }
            return true;
        }

        private boolean isInSpecialRange(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();
            int verticalRange = deleteBitmap.getHeight();
            int horizontalRange = getWidth() - deleteBitmap.getWidth();
            deleteBitmap.getHeight();
            return x >= horizontalRange && y <= verticalRange;
        }

        private void openGallery() {
            switch (getRole()) {
                case PLACER:
                    GalleryFinal.openGallerySingle(1,
                                                   getFunctionConfig(),
                                                   this);
                    break;
                case ADDER:
                    Log.i(TAG,
                          "openGallery: adder");
                    GalleryFinal.openGalleryMuti(10,
                                                 getFunctionConfig(),
                                                 this);
                    break;
            }
        }

        private FunctionConfig getFunctionConfig() {
            return new FunctionConfig.Builder().setEnableCamera(true)
                                               .setFilter(configs.alreadyAddPhoto)
                                               .setMutiSelectMaxSize(configs.surplusSelectLimit)
                                               .build();
        }

        @Override
        public void onHanlderSuccess(int reqeustCode,
                                     List<PhotoInfo> resultList) {
            if (getRole() == PLACER) {
                String photoPath = resultList.get(0)
                                             .getPhotoPath();
                DeleterImageView.this.setImageURI(Uri.fromFile(new File(photoPath)));
                deleterLayoutCallback.placerAdd(photoPath,
                                                DeleterImageView.this);
            } else {
                for (PhotoInfo photoInfo : resultList) {
                    deleterLayoutCallback.photoAdd(photoInfo.getPhotoPath(),
                                                   DeleterImageView.this);
                }
            }
        }

        @Override
        public void onHanlderFailure(int requestCode,
                                     String errorMsg) {

        }

        @Override
        public void onPhotoSuccess(String path,
                                   int requestCode) {
            if (deleterLayoutCallback != null) {
                DeleterImageView.this.photoPath = path;
                if (getRole() == PLACER) {
                    Picasso.with(getContext())
                           .load(new File(path))
                           .resize(150,
                                   150)
                           .into(DeleterImageView.this);
                    DeleterImageView.this.setImageURI(Uri.fromFile(new File(path)));
                    deleterLayoutCallback.placerAdd(path,
                                                    DeleterImageView.this);
                } else if (getRole() == ADDER) {
                    deleterLayoutCallback.photoAdd(photoPath,
                                                   DeleterImageView.this);
                }
            }
        }

        @Override
        public void onOptsSelect(PhotoPopupOpts opts) {
            switch (opts) {
                case TAKE_PHOTO:
                    CameraUtils.openCamera(getContext(),
                                           this,
                                           10086);
                    break;
                case ALBUM:
                    openGallery();
                    break;
            }
        }
    }
}