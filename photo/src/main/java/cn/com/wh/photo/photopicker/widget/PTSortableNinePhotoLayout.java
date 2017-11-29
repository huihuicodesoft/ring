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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.os.ParcelableCompat;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;
import android.support.v4.view.AbsSavedState;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.com.wh.photo.R;
import cn.com.wh.photo.adapter.RecyclerViewAdapter;
import cn.com.wh.photo.adapter.RecyclerViewHolder;
import cn.com.wh.photo.adapter.ViewHolderHelper;
import cn.com.wh.photo.adapter.listener.OnItemChildClickListener;
import cn.com.wh.photo.adapter.listener.OnRVItemClickListener;
import cn.com.wh.photo.photopicker.decoration.SpaceItemDecoration;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.util.FileTypeUtils;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtils;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;
import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_IDLE;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/7/8 下午4:51
 * 描述:拖拽排序九宫格图片控件
 */
public class PTSortableNinePhotoLayout extends RecyclerView implements OnItemChildClickListener, OnRVItemClickListener {
    private PhotoAdapter mPhotoAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private Delegate mDelegate;
    private GridLayoutManager mGridLayoutManager;

    private boolean mPlusEnable;
    private boolean mSortable;
    private int mDeleteDrawableResId;
    private boolean mDeleteDrawableOverlapQuarter;
    private int mPhotoTopRightMargin;
    private int mMaxItemCount;
    private int mItemSpanCount;
    private int mPlusDrawableResId;
    private int mItemCornerRadius;
    private int mItemWhiteSpacing;
    private int mOtherWhiteSpacing;
    private int mPlaceholderDrawableResId;
    private boolean mEditable;

    private int mItemWidth;

    public PTSortableNinePhotoLayout(Context context) {
        this(context, null);
    }

    public PTSortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PTSortableNinePhotoLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDefaultAttrs();
        initCustomAttrs(context, attrs);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs() {
        mPlusEnable = true;
        mSortable = true;
        mEditable = true;
        mDeleteDrawableResId = R.mipmap.ic_delete;
        mDeleteDrawableOverlapQuarter = false;
        mMaxItemCount = 9;
        mItemSpanCount = 3;
        mItemWidth = 0;
        mItemCornerRadius = 0;
        mPlusDrawableResId = R.mipmap.ic_plus;
        mItemWhiteSpacing = PhotoPickerUtils.dp2px(4);
        mPlaceholderDrawableResId = R.mipmap.ic_holder_light;
        mOtherWhiteSpacing = PhotoPickerUtils.dp2px(100);
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PTSortableNinePhotoLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_plusEnable) {
            mPlusEnable = typedArray.getBoolean(attr, mPlusEnable);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_sortable) {
            mSortable = typedArray.getBoolean(attr, mSortable);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_deleteDrawable) {
            mDeleteDrawableResId = typedArray.getResourceId(attr, mDeleteDrawableResId);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_deleteDrawableOverlapQuarter) {
            mDeleteDrawableOverlapQuarter = typedArray.getBoolean(attr, mDeleteDrawableOverlapQuarter);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_maxItemCount) {
            mMaxItemCount = typedArray.getInteger(attr, mMaxItemCount);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_itemSpanCount) {
            mItemSpanCount = typedArray.getInteger(attr, mItemSpanCount);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_plusDrawable) {
            mPlusDrawableResId = typedArray.getResourceId(attr, mPlusDrawableResId);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_itemCornerRadius) {
            mItemCornerRadius = typedArray.getDimensionPixelSize(attr, 0);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_itemWhiteSpacing) {
            mItemWhiteSpacing = typedArray.getDimensionPixelSize(attr, mItemWhiteSpacing);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_otherWhiteSpacing) {
            mOtherWhiteSpacing = typedArray.getDimensionPixelOffset(attr, mOtherWhiteSpacing);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_placeholderDrawable) {
            mPlaceholderDrawableResId = typedArray.getResourceId(attr, mPlaceholderDrawableResId);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_editable) {
            mEditable = typedArray.getBoolean(attr, mEditable);
        } else if (attr == R.styleable.PTSortableNinePhotoLayout_snpl_itemWidth) {
            mItemWidth = typedArray.getDimensionPixelSize(attr, mItemWidth);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        if (mItemWidth == 0) {
            mItemWidth = (PhotoPickerUtils.getScreenWidth() - mOtherWhiteSpacing) / mItemSpanCount;
        } else {
            mItemWidth += mItemWhiteSpacing;
        }

        setOverScrollMode(OVER_SCROLL_NEVER);
        mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback());
        mItemTouchHelper.attachToRecyclerView(this);

        mGridLayoutManager = new GridLayoutManager(getContext(), mItemSpanCount);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(new SpaceItemDecoration(mItemWhiteSpacing / 2));

        calculatePhotoTopRightMargin();

        mPhotoAdapter = new PhotoAdapter(this);
        mPhotoAdapter.setOnItemChildClickListener(this);
        mPhotoAdapter.setOnRVItemClickListener(this);
        setAdapter(mPhotoAdapter);
    }

    /**
     * 设置是否可拖拽排序，默认值为 true
     *
     * @param sortable
     */
    public void setSortable(boolean sortable) {
        mSortable = sortable;
    }

    /**
     * 获取是否可拖拽排序
     *
     * @return
     */
    public boolean isSortable() {
        return mSortable;
    }

    /**
     * 设置是否可编辑，默认值为 true
     *
     * @param editable
     */
    public void setEditable(boolean editable) {
        mEditable = editable;
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 获取是否可编辑
     *
     * @return
     */
    public boolean isEditable() {
        return mEditable;
    }

    /**
     * 设置删除按钮图片资源id，默认值为
     *
     * @param deleteDrawableResId
     */
    public void setDeleteDrawableResId(@DrawableRes int deleteDrawableResId) {
        mDeleteDrawableResId = deleteDrawableResId;
        calculatePhotoTopRightMargin();
    }

    /**
     * 设置删除按钮是否重叠四分之一，默认值为 false
     *
     * @param deleteDrawableOverlapQuarter
     */
    public void setDeleteDrawableOverlapQuarter(boolean deleteDrawableOverlapQuarter) {
        mDeleteDrawableOverlapQuarter = deleteDrawableOverlapQuarter;
        calculatePhotoTopRightMargin();
    }

    /**
     * 计算图片右上角 margin
     */
    private void calculatePhotoTopRightMargin() {
        if (mDeleteDrawableOverlapQuarter) {
            int deleteDrawableWidth = BitmapFactory.decodeResource(getResources(), mDeleteDrawableResId).getWidth();
            int deleteDrawablePadding = getResources().getDimensionPixelOffset(R.dimen.photo_delete_padding);
            mPhotoTopRightMargin = deleteDrawablePadding + deleteDrawableWidth / 2;
        } else {
            mPhotoTopRightMargin = 0;
        }
    }

    /**
     * 设置可选择图片的总张数,默认值为 9
     *
     * @param maxItemCount
     */
    public void setMaxItemCount(int maxItemCount) {
        mMaxItemCount = maxItemCount;
    }

    /**
     * 获取选择的图片的最大张数
     *
     * @return
     */
    public int getMaxItemCount() {
        return mMaxItemCount;
    }

    /**
     * 设置列数,默认值为 3
     *
     * @param itemSpanCount
     */
    public void setItemSpanCount(int itemSpanCount) {
        mItemSpanCount = itemSpanCount;
        mGridLayoutManager.setSpanCount(mItemSpanCount);
    }

    /**
     * 设置添加按钮图片，默认值为 R.mipmap.ic_plus
     *
     * @param plusDrawableResId
     */
    public void setPlusDrawableResId(@DrawableRes int plusDrawableResId) {
        mPlusDrawableResId = plusDrawableResId;
    }

    /**
     * 设置 Item 条目圆角尺寸，默认值为 0dp
     *
     * @param itemCornerRadius
     */
    public void setItemCornerRadius(int itemCornerRadius) {
        mItemCornerRadius = itemCornerRadius;
    }

    /**
     * 设置图片路径数据集合
     *
     * @param photos
     */
    public void setData(ArrayList<String> photos) {
        mPhotoAdapter.setData(photos);
    }

    /**
     * 在集合尾部添加更多数据集合
     *
     * @param photos
     */
    public void addMoreData(ArrayList<String> photos) {
        if (photos != null) {
            mPhotoAdapter.getData().addAll(photos);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 在集合的尾部添加一条数据
     *
     * @param photo
     */
    public void addLastItem(String photo) {
        if (!TextUtils.isEmpty(photo)) {
            mPhotoAdapter.getData().add(photo);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spanCount = mItemSpanCount;
        int itemCount = mPhotoAdapter.getItemCount();
        if (itemCount > 0 && itemCount < mItemSpanCount) {
            spanCount = itemCount;
        }
        mGridLayoutManager.setSpanCount(spanCount);

        int expectWidth = mItemWidth * spanCount;
        int expectHeight = 0;
        if (itemCount > 0) {
            int rowCount = (itemCount - 1) / spanCount + 1;
            expectHeight = mItemWidth * rowCount;
        }

        int width = resolveSize(expectWidth, widthMeasureSpec);
        int height = resolveSize(expectHeight, heightMeasureSpec);
        width = Math.min(width, expectWidth);
        height = Math.min(height, expectHeight);

        setMeasuredDimension(width, height);
    }

    /**
     * 获取图片路径数据集合
     *
     * @return
     */
    public ArrayList<String> getData() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mPhotoAdapter.removeItem(position);
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getItemCount() {
        return mPhotoAdapter.getData().size();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (mDelegate != null) {
            mDelegate.onClickDeleteNinePhotoItem(this, childView, position, mPhotoAdapter.getItem(position), (ArrayList<String>) mPhotoAdapter.getData());
        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {
        if (mPhotoAdapter.isPlusItem(position)) {
            if (mDelegate != null) {
                mDelegate.onClickAddNinePhotoItem(this, itemView, position, (ArrayList<String>) mPhotoAdapter.getData());
            }
        } else {
            if (mDelegate != null && ViewCompat.getScaleX(itemView) <= 1.0f) {
                mDelegate.onClickNinePhotoItem(this, itemView, position, mPhotoAdapter.getItem(position), (ArrayList<String>) mPhotoAdapter.getData());
            }
        }
    }

    /**
     * 设置是否显示加号
     *
     * @param plusEnable
     */
    public void setPlusEnable(boolean plusEnable) {
        mPlusEnable = plusEnable;
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 是否显示加号按钮
     *
     * @return
     */
    public boolean isPlusEnable() {
        return mPlusEnable;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    private class PhotoAdapter extends RecyclerViewAdapter<String> {
        private int mImageSize;

        public PhotoAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_nine_photo);
            mImageSize = PhotoPickerUtils.getScreenWidth() / (mItemSpanCount > 3 ? 8 : 6);
        }

        @Override
        protected void setItemChildListener(ViewHolderHelper helper, int viewType) {
            helper.setItemChildClickListener(R.id.iv_item_nine_photo_flag);
        }

        @Override
        public int getItemCount() {
            if (mEditable && mPlusEnable && super.getItemCount() < mMaxItemCount) {
                return super.getItemCount() + 1;
            }

            return super.getItemCount();
        }

        @Override
        public String getItem(int position) {
            if (isPlusItem(position)) {
                return null;
            }

            return super.getItem(position);
        }

        public boolean isPlusItem(int position) {
            return mEditable && mPlusEnable && super.getItemCount() < mMaxItemCount && position == getItemCount() - 1;
        }

        @Override
        protected void fillData(ViewHolderHelper helper, int position, String model) {
            MarginLayoutParams params = (MarginLayoutParams) helper.getView(R.id.iv_item_nine_photo_photo).getLayoutParams();
            params.setMargins(0, mPhotoTopRightMargin, mPhotoTopRightMargin, 0);

            if (mItemCornerRadius > 0) {
                PTImageView imageView = helper.getView(R.id.iv_item_nine_photo_photo);
                imageView.setCornerRadius(mItemCornerRadius);
            }

            if (isPlusItem(position)) {
                helper.setVisibility(R.id.iv_item_nine_photo_flag, View.GONE);
                helper.setImageResource(R.id.iv_item_nine_photo_photo, mPlusDrawableResId);
            } else {
                if (mEditable) {
                    helper.setVisibility(R.id.iv_item_nine_photo_flag, View.VISIBLE);
                    helper.setImageResource(R.id.iv_item_nine_photo_flag, mDeleteDrawableResId);
                } else {
                    helper.setVisibility(R.id.iv_item_nine_photo_flag, View.GONE);
                }
                Image.display(helper.getImageView(R.id.iv_item_nine_photo_photo), mPlaceholderDrawableResId, model, mImageSize);
                if (FileTypeUtils.isGif(model)) {
                    helper.setVisibility(R.id.iv_item_nine_photo_photo_type, View.VISIBLE);
                    helper.setImageResource(R.id.iv_item_nine_photo_photo_type, R.mipmap.ic_delete);
                } else {
                    helper.setVisibility(R.id.iv_item_nine_photo_photo_type, View.GONE);
                }
            }
        }
    }

    private class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

        @Override
        public boolean isLongPressDragEnabled() {
            return mEditable && mSortable && mPhotoAdapter.getData().size() > 1;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            if (mPhotoAdapter.isPlusItem(viewHolder.getAdapterPosition())) {
                dragFlags = ACTION_STATE_IDLE;
            }
            int swipeFlags = ACTION_STATE_IDLE;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            if (source.getItemViewType() != target.getItemViewType() || mPhotoAdapter.isPlusItem(target.getAdapterPosition())) {
                return false;
            }
            mPhotoAdapter.moveItem(source.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ACTION_STATE_IDLE) {
                ViewCompat.setScaleX(viewHolder.itemView, 1.2f);
                ViewCompat.setScaleY(viewHolder.itemView, 1.2f);
                ((RecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter(getResources().getColor(R.color.photo_selected_mask));
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            ViewCompat.setScaleX(viewHolder.itemView, 1.0f);
            ViewCompat.setScaleY(viewHolder.itemView, 1.0f);
            ((RecyclerViewHolder) viewHolder).getViewHolderHelper().getImageView(R.id.iv_item_nine_photo_photo).setColorFilter(null);
            super.clearView(recyclerView, viewHolder);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.booleanArray =  new boolean[]{mPlusEnable, mSortable, mDeleteDrawableOverlapQuarter, mEditable};
        savedState.deleteDrawableResId = mDeleteDrawableResId;
        savedState.photoTopRightMargin = mPhotoTopRightMargin;
        savedState.maxItemCount = mMaxItemCount;
        savedState.itemSpanCount = mItemSpanCount;
        savedState.plusDrawableResId = mPlusDrawableResId;
        savedState.itemCornerRadius = mItemCornerRadius;
        savedState.itemWhiteSpacing = mItemWhiteSpacing;
        savedState.otherWhiteSpacing = mOtherWhiteSpacing;
        savedState.placeholderDrawableResId = mPlaceholderDrawableResId;
        savedState.itemWidth = mItemWidth;
        savedState.data = mPhotoAdapter.getData();
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mPlusEnable = savedState.booleanArray[0];
        mSortable = savedState.booleanArray[1];
        mDeleteDrawableOverlapQuarter = savedState.booleanArray[2];
        mEditable = savedState.booleanArray[3];
        mDeleteDrawableResId = savedState.deleteDrawableResId;
        mMaxItemCount = savedState.maxItemCount;
        mItemSpanCount = savedState.itemSpanCount;
        mItemWidth = savedState.itemWidth;
        mItemCornerRadius = savedState.itemCornerRadius;
        mPlusDrawableResId = savedState.plusDrawableResId;
        mItemWhiteSpacing = savedState.itemWhiteSpacing;
        mPlaceholderDrawableResId = savedState.placeholderDrawableResId;
        mOtherWhiteSpacing = savedState.otherWhiteSpacing;
        setData((ArrayList<String>)savedState.data);
    }

    public interface Delegate {
        void onClickAddNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models);

        void onClickDeleteNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models);

        void onClickNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models);
    }

    /**
     * This is public so that the CREATOR can be access on cold launch.
     *
     * @hide
     */
    @RestrictTo(LIBRARY_GROUP)
    public static class SavedState extends AbsSavedState {
        private boolean[] booleanArray = new boolean[4];
        private int deleteDrawableResId;
        private int photoTopRightMargin;
        private int maxItemCount;
        private int itemSpanCount;
        private int plusDrawableResId;
        private int itemCornerRadius;
        private int itemWhiteSpacing;
        private int otherWhiteSpacing;
        private int placeholderDrawableResId;
        private int itemWidth;
        private List data = new ArrayList();

        /**
         * called by CREATOR
         */
        SavedState(Parcel in, ClassLoader loader) {
            super(in, loader);
            in.readBooleanArray(booleanArray);
            deleteDrawableResId = in.readInt();
            photoTopRightMargin = in.readInt();
            maxItemCount = in.readInt();
            itemSpanCount = in.readInt();
            plusDrawableResId = in.readInt();
            itemCornerRadius = in.readInt();
            itemWhiteSpacing = in.readInt();
            otherWhiteSpacing = in.readInt();
            placeholderDrawableResId = in.readInt();
            itemWidth = in.readInt();
            in.readList(data, loader);
        }

        /**
         * Called by onSaveInstanceState
         */
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBooleanArray(booleanArray);
            dest.writeInt(deleteDrawableResId);
            dest.writeInt(photoTopRightMargin);
            dest.writeInt(maxItemCount);
            dest.writeInt(itemSpanCount);
            dest.writeInt(plusDrawableResId);
            dest.writeInt(itemCornerRadius);
            dest.writeInt(itemWhiteSpacing);
            dest.writeInt(otherWhiteSpacing);
            dest.writeInt(placeholderDrawableResId);
            dest.writeInt(itemWidth);
            dest.writeList(data);
        }

        public static final Creator<SavedState> CREATOR = ParcelableCompat.newCreator(
                new ParcelableCompatCreatorCallbacks<SavedState>() {
                    @Override
                    public SavedState createFromParcel(Parcel in, ClassLoader loader) {
                        return new SavedState(in, loader);
                    }

                    @Override
                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                });
    }
}