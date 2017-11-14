package cn.com.wh.ring.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import java.util.ArrayList;
import java.util.List;

import cn.com.wh.photo.GlideApp;
import cn.com.wh.photo.adapter.listener.OnNoDoubleClickListener;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.PictureInfo;
import cn.com.wh.ring.utils.ImageUtils;
import cn.com.wh.ring.utils.NetworkUtils;

public class PhotosWidget extends ViewGroup implements OnLongClickListener, OnTouchListener {
    public static final int MAX_IMAGES = 9;

    private ItemsLayoutType mItemsLayoutType;
    private int itemSpace;
    private List<PictureInfo> mPictureInfoList;

    private OnNoDoubleClickListener mOnNoDoubleClickListener = new OnNoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            if (onInterceptorClick(v)) return;

            for (int i = 0; i < getChildCount(); i++) {
                if (getChildAt(i) == v) {
                    if (mPhotoClickListener != null) {
                        mPhotoClickListener.onPhotoClick(PhotosWidget.this, v, i);
                    }
                    break;
                }
            }
        }
    };

    public PhotosWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        itemSpace = getResources().getDimensionPixelSize(R.dimen.photo_widget_itemWidth);
    }

    public void setImageInfo(List<PictureInfo> imageInfo) {
        mPictureInfoList = imageInfo;
        List<String> imageUrlList = new ArrayList<>();
        if (imageInfo != null && imageInfo.size() == 1) {
            PictureInfo info = imageInfo.get(0);
            imageUrlList.add(info.getMediumUrl());
        } else if (imageInfo != null) {
            if (NetworkUtils.NETWORK_STATE.WIFI.equals(NetworkUtils.getNetType())) {
                //wifi下显示中图
                for (PictureInfo pi : imageInfo) {
                    imageUrlList.add(pi.getMediumUrl());
                }
            } else {
                //非wifi下显示小图
                for (PictureInfo pi : imageInfo) {
                    imageUrlList.add(pi.getSmallUrl());
                }
            }
        }
        setImagePath(imageUrlList);
    }

    private void setImagePath(List<String> images) {
        if (images.size() == 1) {
            ImageView iv = (ImageView) getChildAt(0);
            GlideApp.with(getContext()).load(images.get(0))
                    .error(R.color.nine_photo_default)
                    .placeholder(R.color.nine_photo_default)
                    .fitCenter().into(iv);
            requestLayout();
        } else {
            for (int i = 0; i < images.size() && i < MAX_IMAGES; i++) {
                ImageView iv = (ImageView) getChildAt(i);
                GlideApp.with(getContext()).load(images.get(i))
                        .error(R.color.nine_photo_default)
                        .placeholder(R.color.nine_photo_default)
                        .centerCrop().into(iv);
            }
        }
    }

    public void setImagesCapacity(int capacity) {
        initImages(capacity);
    }

    private void initImages(int count) {
        if (getChildCount() > 0) {
            removeAllViews();
        }

        if (count == 1) {
            ImageView iv = new ImageView(getContext());
            iv.setScaleType(ScaleType.FIT_CENTER);
            iv.setOnClickListener(mOnNoDoubleClickListener);
            iv.setOnLongClickListener(this);
            iv.setOnTouchListener(this);
            addView(iv);
            setItemsType(getChildCount());
            return;
        }

        for (int i = 0; i < count && i < MAX_IMAGES; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setScaleType(ScaleType.CENTER_CROP);
            iv.setOnClickListener(mOnNoDoubleClickListener);
            iv.setOnLongClickListener(this);
            iv.setOnTouchListener(this);
            addView(iv);
        }
        setItemsType(getChildCount());
    }

    private void setItemsType(int childCount) {
        if (childCount == 0) {
            mItemsLayoutType = ItemsLayoutType.ZERO;
        } else if (childCount == 1) {
            mItemsLayoutType = ItemsLayoutType.ONLY_ONE;
        } else if (childCount == 4) {
            mItemsLayoutType = ItemsLayoutType.JUST_FOUR;
        } else {
            mItemsLayoutType = ItemsLayoutType.NORMAL;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mItemsLayoutType != null) {
            int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);

            for (int i = 0; i < getChildCount(); i++) {
                measureChildWithMargins(getChildAt(i), widthMeasureSpec, 0, heightMeasureSpec, 0);
            }
            int layoutHeight = getLayoutHeight(layoutWidth);
            setMeasuredDimension(
                    resolveSize(layoutWidth, widthMeasureSpec),
                    resolveSize(layoutHeight, heightMeasureSpec));
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void measureChildWithMargins(View child,
                                           int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        int itemWidth = getItemWidth(MeasureSpec.getSize(parentWidthMeasureSpec));
        int childMeasureSpec = getChildMeasureSpec(MeasureSpec.EXACTLY, 0, itemWidth);
        if (mItemsLayoutType == ItemsLayoutType.ONLY_ONE) {
            PictureInfo pictureInfo = mPictureInfoList.get(0);
            if (pictureInfo != null) {
                int imageWidth = pictureInfo.getWidth();
                int imageHeight = pictureInfo.getHeight();
                if (imageHeight >= imageWidth) {
                    imageWidth = Math.round((itemWidth * 2.0f) / imageHeight * imageWidth);
                    imageHeight = itemWidth * 2;
                } else {
                    imageHeight = Math.round((itemWidth * 2.0f) / imageWidth * imageHeight);
                    imageWidth = itemWidth * 2;
                }
                int widthMeasureSpec = getChildMeasureSpec(MeasureSpec.EXACTLY, 0, imageWidth);
                int heightMeasureSpec = getChildMeasureSpec(MeasureSpec.EXACTLY, 0, imageHeight);
                child.measure(widthMeasureSpec, heightMeasureSpec);
            } else {
                int measureSpec = getChildMeasureSpec(MeasureSpec.EXACTLY, 0, itemWidth * 2);
                child.measure(measureSpec, measureSpec);
            }
        } else {
            child.measure(childMeasureSpec, childMeasureSpec);
        }
    }

    private int getItemWidth(int parentWidth) {
        return (parentWidth - itemSpace * 2) / 3;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (mItemsLayoutType != null) {
            switch (mItemsLayoutType) {
                case ZERO:
                    break;
                case ONLY_ONE:
                    layoutItem();
                    break;
                case JUST_FOUR:
                    layoutItemsByMode(2);
                    break;
                case NORMAL:
                    layoutItemsByMode(3);
                    break;
            }
        }
    }

    private void layoutItem() {
        int l = 0;
        int t = 0;
        int r = getChildAt(0).getMeasuredWidth();
        int b = getChildAt(0).getMeasuredHeight();
        getChildAt(0).layout(l, t, r, b);
    }

    private void layoutItemsByMode(int mode) {
        final int itemWidth = getItemWidth(getWidth());
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            int l = (i % mode) * (itemSpace + itemWidth);
            int t = (i / mode) * (itemSpace + itemWidth);
            int r = l + itemWidth;
            int b = t + itemWidth;
            v.layout(l, t, r, b);
        }
    }

    private int getLayoutHeight(int parentWidth) {
        int result = 0;
        switch (mItemsLayoutType) {
            case ZERO:
                result = 0;
                break;
            case ONLY_ONE:
                result = getChildAt(0).getMeasuredHeight();
                //尽早设置最有可能的值，减少重新设置尺寸带来的卡顿
                if (result == 0) result = getItemWidth(parentWidth) * 2;
                break;
            case JUST_FOUR:
                result = getItemWidth(parentWidth) * 2 + itemSpace;
                break;
            case NORMAL:
                final int row = (getChildCount() - 1) / 3 + 1;
                result = getItemWidth(parentWidth) * row + (row - 1) * itemSpace;
                break;
        }
        return result;
    }

    private enum ItemsLayoutType {
        ZERO, ONLY_ONE, JUST_FOUR, NORMAL
    }

    public interface OnPhotoClickListener {
        void onPhotoClick(ViewGroup parent, View view, int position);
    }

    private OnPhotoClickListener mPhotoClickListener;

    public void setPhotoClickListener(OnPhotoClickListener listener) {
        mPhotoClickListener = listener;
    }

    private boolean onInterceptorClick(View v) {
        if (v instanceof ImageView) {
            return ImageUtils.isEmptyDrawable(((ImageView) v));
        }
        return false;
    }

    public interface OnPhotoLongClickListener {
        void onPhotoLongClick(ViewGroup parent, View view, int position);

    }

    private OnPhotoLongClickListener mPhotoLongClickListener;

    public void setPhotoLongClickListener(OnPhotoLongClickListener listener) {
        mPhotoLongClickListener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == v) {
                if (mPhotoLongClickListener != null) {
                    mPhotoLongClickListener.onPhotoLongClick(this, v, i);
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView iv = (ImageView) v;
        Drawable drawable = iv.getDrawable();
        if (drawable == null) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                iv.invalidate();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                drawable.clearColorFilter();
                iv.invalidate();
                break;
            }
        }
        return false;
    }
}
