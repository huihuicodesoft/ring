/**
 * Copyright 2016 bingoogolapple
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.com.wh.photo.photopicker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cn.com.wh.photo.R;
import cn.com.wh.photo.adapter.AdapterViewAdapter;
import cn.com.wh.photo.adapter.ViewHolderHelper;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.util.FileTypeUtils;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtils;

/**
 * 描述:九宫格图片控件
 */
public class PTNinePhotoLayout extends FrameLayout implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final int ITEM_NUM_COLUMNS = 3;
    private PhotoAdapter mPhotoAdapter;
    private PTImageView mPhotoIv;
    private HeightWrapGridView mPhotoGv;
    private Delegate mDelegate;
    private int mCurrentClickItemPosition;

    private int mItemCornerRadius;
    private boolean mShowAsLargeWhenOnlyOne;
    private int mItemWhiteSpacing;
    private int mOtherWhiteSpacing;
    private int mPlaceholderDrawableResId;
    private int mItemSpanCount;

    private int mItemWidth;

    public PTNinePhotoLayout(Context context) {
        this(context, null);
    }

    public PTNinePhotoLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PTNinePhotoLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDefaultAttrs();
        initCustomAttrs(context, attrs);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs() {
        mItemWidth = 0;
        mShowAsLargeWhenOnlyOne = true;
        mItemCornerRadius = 0;
        mItemWhiteSpacing = PhotoPickerUtils.dp2px(4);
        mPlaceholderDrawableResId = R.mipmap.ic_holder_light;
        mOtherWhiteSpacing = PhotoPickerUtils.dp2px(100);
        mItemSpanCount = 3;
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PTNinePhotoLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.PTNinePhotoLayout_npl_showAsLargeWhenOnlyOne) {
            mShowAsLargeWhenOnlyOne = typedArray.getBoolean(attr, mShowAsLargeWhenOnlyOne);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_itemCornerRadius) {
            mItemCornerRadius = typedArray.getDimensionPixelSize(attr, mItemCornerRadius);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_itemWhiteSpacing) {
            mItemWhiteSpacing = typedArray.getDimensionPixelSize(attr, mItemWhiteSpacing);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_otherWhiteSpacing) {
            mOtherWhiteSpacing = typedArray.getDimensionPixelOffset(attr, mOtherWhiteSpacing);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_placeholderDrawable) {
            mPlaceholderDrawableResId = typedArray.getResourceId(attr, mPlaceholderDrawableResId);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_itemWidth) {
            mItemWidth = typedArray.getDimensionPixelSize(attr, mItemWidth);
        } else if (attr == R.styleable.PTNinePhotoLayout_npl_itemSpanCount) {
            mItemSpanCount = typedArray.getInteger(attr, mItemSpanCount);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        if (mItemWidth == 0) {
            mItemWidth = (PhotoPickerUtils.getScreenWidth() - mOtherWhiteSpacing - (mItemSpanCount - 1) * mItemWhiteSpacing) / mItemSpanCount;
        }

        mPhotoIv = new PTImageView(getContext());
        mPhotoIv.setClickable(true);
        mPhotoIv.setOnClickListener(this);

        mPhotoGv = new HeightWrapGridView(getContext());
        mPhotoGv.setHorizontalSpacing(mItemWhiteSpacing);
        mPhotoGv.setVerticalSpacing(mItemWhiteSpacing);
        mPhotoGv.setNumColumns(ITEM_NUM_COLUMNS);
        mPhotoGv.setOnItemClickListener(this);
        mPhotoAdapter = new PhotoAdapter(getContext());
        mPhotoGv.setAdapter(mPhotoAdapter);

        addView(mPhotoIv, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mPhotoGv);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        mCurrentClickItemPosition = position;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    @Override
    public void onClick(View view) {
        mCurrentClickItemPosition = 0;
        if (mDelegate != null) {
            mDelegate.onClickNinePhotoItem(this, view, mCurrentClickItemPosition, mPhotoAdapter.getItem(mCurrentClickItemPosition), mPhotoAdapter.getData());
        }
    }

    /**
     * 设置图片路径数据集合
     *
     * @param photos
     */
    public void setData(List<String> photos) {
        if (photos.size() == 0) {
            setVisibility(GONE);
        } else {
            setVisibility(VISIBLE);

            if (photos.size() == 1 && mShowAsLargeWhenOnlyOne) {
                mPhotoGv.setVisibility(GONE);
                mPhotoAdapter.setData(photos);
                mPhotoIv.setVisibility(VISIBLE);

                int size = mItemWidth * 2 + mItemWhiteSpacing + mItemWidth / 4;
                mPhotoIv.setMaxWidth(size);
                mPhotoIv.setMaxHeight(size);

                if (mItemCornerRadius > 0) {
                    mPhotoIv.setCornerRadius(mItemCornerRadius);
                }

                Image.display(mPhotoIv, mPlaceholderDrawableResId, photos.get(0), size);
            } else {
                mPhotoIv.setVisibility(GONE);
                mPhotoGv.setVisibility(VISIBLE);
                ViewGroup.LayoutParams layoutParams = mPhotoGv.getLayoutParams();

                if (mItemSpanCount > 3) {
                    int itemSpanCount = photos.size() < mItemSpanCount ? photos.size() : mItemSpanCount;
                    mPhotoGv.setNumColumns(itemSpanCount);
                    layoutParams.width = mItemWidth * itemSpanCount + (itemSpanCount - 1) * mItemWhiteSpacing;
                } else {
                    if (photos.size() == 1) {
                        mPhotoGv.setNumColumns(1);
                        layoutParams.width = mItemWidth * 1;
                    } else if (photos.size() == 2) {
                        mPhotoGv.setNumColumns(2);
                        layoutParams.width = mItemWidth * 2 + mItemWhiteSpacing;
                    } else if (photos.size() == 4) {
                        mPhotoGv.setNumColumns(2);
                        layoutParams.width = mItemWidth * 2 + mItemWhiteSpacing;
                    } else {
                        mPhotoGv.setNumColumns(3);
                        layoutParams.width = mItemWidth * 3 + 2 * mItemWhiteSpacing;
                    }
                }

                mPhotoGv.setLayoutParams(layoutParams);
                mPhotoAdapter.setData(photos);
            }
        }
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public ArrayList<String> getData() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    public int getItemCount() {
        return mPhotoAdapter.getCount();
    }

    public String getCurrentClickItem() {
        return mPhotoAdapter.getItem(mCurrentClickItemPosition);
    }

    public int getCurrentClickItemPosition() {
        return mCurrentClickItemPosition;
    }

    private class PhotoAdapter extends AdapterViewAdapter<String> {
        private int mImageSize;

        public PhotoAdapter(Context context) {
            super(context, R.layout.item_nine_photo);
            mImageSize = PhotoPickerUtils.getScreenWidth() / (mItemSpanCount > 3 ? 8 : 6);
        }

        @Override
        protected void fillData(ViewHolderHelper helper, int position, String model) {
            if (mItemCornerRadius > 0) {
                PTImageView imageView = helper.getView(R.id.iv_item_nine_photo_photo);
                imageView.setCornerRadius(mItemCornerRadius);
            }

            Image.display(helper.getImageView(R.id.iv_item_nine_photo_photo), mPlaceholderDrawableResId, model, mImageSize);
            if (FileTypeUtils.isGif(model)) {
                helper.setVisibility(R.id.iv_item_nine_photo_photo_type,View.VISIBLE);
                helper.setImageResource(R.id.iv_item_nine_photo_photo_type, R.mipmap.ic_delete);
            } else {
                helper.setVisibility(R.id.iv_item_nine_photo_photo_type,View.GONE);
            }
        }
    }

    public interface Delegate {
        void onClickNinePhotoItem(PTNinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);
    }
}