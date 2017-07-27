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
package cn.com.wh.ring.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.photo.adapter.listener.OnNoDoubleClickListener;
import cn.com.wh.photo.photopicker.adapter.PhotoPageAdapter;
import cn.com.wh.photo.photopicker.widget.HackyViewPager;
import cn.com.wh.photo.photoview.PhotoViewAttacher;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:57
 * 描述:图片选择预览界面
 */
public class PhotoPickerPreviewActivity extends TitleActivity implements PhotoViewAttacher.OnViewTapListener {
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_FROM_TAKE_PHOTO = "EXTRA_IS_FROM_TAKE_PHOTO";

    @BindView(R.id.photo_picker_preview_content_hvp)
    HackyViewPager mContentHvp;
    @BindView(R.id.photo_picker_preview_choose_rl)
    RelativeLayout mChooseRl;
    @BindView(R.id.photo_picker_preview_choose_tv)
    TextView mChooseTv;

    @BindColor(R.color.back_photo_title)
    int mTitleBackColor;
    @BindDrawable(R.drawable.photo_confirm_selector)
    Drawable mConfirmDrawable;

    private ArrayList<String> mSelectedImages;
    private PhotoPageAdapter mPhotoPageAdapter;
    private int mMaxChooseCount = 1;

    private boolean mIsHidden = false;
    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;
    /**
     * 是否是拍完照后跳转过来
     */
    private boolean mIsFromTakePhoto;

    /**
     * @param context         应用程序上下文
     * @param maxChooseCount  图片选择张数的最大值
     * @param selectedImages  当前已选中的图片路径集合，可以传null
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @param isFromTakePhoto 是否是拍完照后跳转过来
     * @return
     */
    public static Intent newIntent(Context context, int maxChooseCount, ArrayList<String> selectedImages, ArrayList<String> previewImages, int currentPosition, boolean isFromTakePhoto) {
        Intent intent = new Intent(context, PhotoPickerPreviewActivity.class);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, isFromTakePhoto);
        return intent;
    }

    /**
     * 获取已选择的图片集合
     *
     * @param intent
     * @return
     */
    public static ArrayList<String> getSelectedImages(Intent intent) {
        return intent.getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
    }

    /**
     * 是否是拍照预览
     *
     * @param intent
     * @return
     */
    public static boolean getIsFromTakePhoto(Intent intent) {
        return intent.getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker_preview);
        unbinder = ButterKnife.bind(this);
        initBase();
        initListener();
        processLogic();
    }

    @Override
    protected int getRootResId() {
        return R.layout.activity_title_frame;
    }

    private void initBase() {
        mStatusBar.setBackgroundColor(mTitleBackColor);
        mTitleRl.setBackgroundColor(mTitleBackColor);
        mRightTv.setBackground(mConfirmDrawable);
        mRightTv.setText(R.string.ok);
    }

    protected void initListener() {
        mChooseTv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String currentImage = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
                if (mSelectedImages.contains(currentImage)) {
                    mSelectedImages.remove(currentImage);
                    mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cb_normal, 0, 0, 0);
                    renderTopRightBtn();
                } else {
                    if (mMaxChooseCount == 1) {
                        // 单选
                        mSelectedImages.clear();
                        mSelectedImages.add(currentImage);
                        mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cb_checked, 0, 0, 0);
                        renderTopRightBtn();
                    } else {
                        // 多选

                        if (mMaxChooseCount == mSelectedImages.size()) {
                            ToastUtils.showShortToast(getString(R.string.format_photo_picker_max, mMaxChooseCount));
                        } else {
                            mSelectedImages.add(currentImage);
                            mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cb_checked, 0, 0, 0);
                            renderTopRightBtn();
                        }
                    }
                }
            }
        });

        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handlePageSelectedStatus();
            }
        });

        mRightTv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
                intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    protected void processLogic() {
        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }

        mSelectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);
        if (TextUtils.isEmpty(previewImages.get(0))) {
            // 从BGAPhotoPickerActivity跳转过来时，如果有开启拍照功能，则第0项为""
            previewImages.remove(0);
        }

        // 处理是否是拍完照后跳转过来
        mIsFromTakePhoto = getIntent().getBooleanExtra(EXTRA_IS_FROM_TAKE_PHOTO, false);
        if (mIsFromTakePhoto) {
            // 如果是拍完照后跳转过来，一直隐藏底部选择栏
            mChooseRl.setVisibility(View.INVISIBLE);
        }
        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        mPhotoPageAdapter = new PhotoPageAdapter(this, this, previewImages);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);

        // 过2秒隐藏标题栏和底部选择栏
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hiddenToolBarAndChooseBar();
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, mSelectedImages);
        intent.putExtra(EXTRA_IS_FROM_TAKE_PHOTO, mIsFromTakePhoto);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    private void handlePageSelectedStatus() {
        if (mTitleTv == null || mPhotoPageAdapter == null) {
            return;
        }

        mTitleTv.setText((mContentHvp.getCurrentItem() + 1) + "/" + mPhotoPageAdapter.getCount());
        if (mSelectedImages.contains(mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem()))) {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cb_checked, 0, 0, 0);
        } else {
            mChooseTv.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_cb_normal, 0, 0, 0);
        }
    }

    /**
     * 渲染右上角按钮
     */
    private void renderTopRightBtn() {
        if (mIsFromTakePhoto) {
            mRightTv.setEnabled(true);
            mRightTv.setText(R.string.ok);
        } else if (mSelectedImages.size() == 0) {
            mRightTv.setEnabled(false);
            mRightTv.setText(R.string.ok);
        } else {
            mRightTv.setEnabled(true);
            mRightTv.setText(getString(R.string.format_photo_ok, mSelectedImages.size() + "/" + mMaxChooseCount));
        }
    }

    @Override
    public void onViewTap(View view, float x, float y) {
        if (System.currentTimeMillis() - mLastShowHiddenTime > 500) {
            mLastShowHiddenTime = System.currentTimeMillis();
            if (mIsHidden) {
                showTitleBarAndChooseBar();
            } else {
                hiddenToolBarAndChooseBar();
            }
        }
    }

    private void showTitleBarAndChooseBar() {
        if (mTitleRl != null) {
            ViewCompat.animate(mTitleRl).translationY(0).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = false;
                }
            }).start();
        }

        if (!mIsFromTakePhoto && mChooseRl != null) {
            mChooseRl.setVisibility(View.VISIBLE);
            ViewCompat.setAlpha(mChooseRl, 0);
            ViewCompat.animate(mChooseRl).alpha(1).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    private void hiddenToolBarAndChooseBar() {
        if (mTitleRl != null) {
            ViewCompat.animate(mTitleRl).translationY(-mTitleRl.getHeight()).setInterpolator(new DecelerateInterpolator(2)).setListener(new ViewPropertyAnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(View view) {
                    mIsHidden = true;
                    if (mChooseRl != null) {
                        mChooseRl.setVisibility(View.INVISIBLE);
                    }
                }
            }).start();
        }

        if (!mIsFromTakePhoto) {
            if (mChooseRl != null) {
                ViewCompat.animate(mChooseRl).alpha(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        }
    }

}