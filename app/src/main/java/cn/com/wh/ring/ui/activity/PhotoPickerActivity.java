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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.permission.AndPermission;
import cn.com.wh.permission.PermissionListener;
import cn.com.wh.photo.adapter.listener.OnItemChildClickListener;
import cn.com.wh.photo.adapter.listener.OnNoDoubleClickListener;
import cn.com.wh.photo.adapter.listener.OnRVItemClickListener;
import cn.com.wh.photo.photopicker.adapter.FolderAdapter;
import cn.com.wh.photo.photopicker.adapter.PhotoPickerAdapter;
import cn.com.wh.photo.photopicker.cache.TakePhotoCache;
import cn.com.wh.photo.photopicker.decoration.LineItemDecoration;
import cn.com.wh.photo.photopicker.decoration.SpaceItemDecoration;
import cn.com.wh.photo.photopicker.imageloader.RVOnScrollListener;
import cn.com.wh.photo.photopicker.model.ImageFolderModel;
import cn.com.wh.photo.photopicker.task.LoadPhotoTask;
import cn.com.wh.photo.photopicker.task.PTAsyncTask;
import cn.com.wh.photo.photopicker.util.ImageCaptureManager;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * 描述:图片选择界面
 */
public class PhotoPickerActivity extends TitleActivity implements OnItemChildClickListener, PTAsyncTask.Callback<ArrayList<ImageFolderModel>> {
    private static final String EXTRA_IMAGE_DIR = "EXTRA_IMAGE_DIR";
    private static final String EXTRA_SELECTED_IMAGES = "EXTRA_SELECTED_IMAGES";
    private static final String EXTRA_MAX_CHOOSE_COUNT = "EXTRA_MAX_CHOOSE_COUNT";
    private static final String EXTRA_PAUSE_ON_SCROLL = "EXTRA_PAUSE_ON_SCROLL";
    private static final int ANIM_DURATION = 300;

    /**
     * 拍照的请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /**
     * 预览照片的请求码
     */
    private static final int REQUEST_CODE_PREVIEW = 2;

    private static final int SPAN_COUNT = 3;

    @BindView(R.id.photo_picker_folder_tv)
    TextView mPickerFolderTv;
    @BindView(R.id.photo_picker_content_rv)
    RecyclerView mPhotoRv;
    @BindView(R.id.photo_folder_content_rl)
    RelativeLayout mFolderRL;
    @BindView(R.id.photo_folder_content_back)
    View mFolderBack;
    @BindView(R.id.photo_folder_content_rv)
    RecyclerView mFolderRv;
    @BindView(R.id.loading_pb)
    ProgressBar mLoadingPb;

    @BindColor(R.color.back_photo_title)
    int mTitleBackColor;
    @BindColor(R.color.line)
    int mLineColor;
    @BindDrawable(R.drawable.photo_confirm_selector)
    Drawable mConfirmDrawable;
    @BindString(R.string.permission_storage)
    String mPermissionStorage;
    @BindString(R.string.permission_camera)
    String mPermissionCamera;

    /**
     * 是否可以拍照
     */
    private boolean mTakePhotoEnabled;
    /**
     * 最多选择多少张图片，默认等于1，为单选
     */
    private int mMaxChooseCount = 1;

    /**
     * 图片目录数据集合
     */
    private ArrayList<ImageFolderModel> mImageFolderModels;
    private ImageFolderModel mCurrentImageFolderModel;

    private PhotoPickerAdapter mPicAdapter;
    private FolderAdapter mFolderAdapter;

    private ImageCaptureManager mImageCaptureManager;

    private LoadPhotoTask mLoadPhotoTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);
        unbinder = ButterKnife.bind(this);

        initBase();
        initPhotoRV();
        initFolderRV();
        renderTopRightBtn();

        requestStoragePermission();
    }

    private void initBase() {
        mStatusBar.setBackgroundColor(mTitleBackColor);
        mTitleRl.setBackgroundColor(mTitleBackColor);
        setTitle(R.string.select_photo);
        mPickerFolderTv.setText(R.string.all_image);
        mRightTv.setBackground(mConfirmDrawable);

        mPickerFolderTv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mImageFolderModels != null && mImageFolderModels.size() > 0) {
                    togglePhotoFolderUI();
                }
            }
        });

        mRightTv.setOnClickListener(new OnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                returnSelectedImages(mPicAdapter.getSelectedImages());
            }
        });

        // 获取拍照图片保存目录
        File imageDir = (File) getIntent().getSerializableExtra(EXTRA_IMAGE_DIR);
        if (imageDir != null) {
            mTakePhotoEnabled = true;
            mImageCaptureManager = new ImageCaptureManager(imageDir);
        }

        // 获取图片选择的最大张数
        mMaxChooseCount = getIntent().getIntExtra(EXTRA_MAX_CHOOSE_COUNT, 1);
        if (mMaxChooseCount < 1) {
            mMaxChooseCount = 1;
        }
    }

    private void initFolderRV() {
        mFolderAdapter = new FolderAdapter(mFolderRv);
        mFolderAdapter.setOnRVItemClickListener(new OnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                reloadPhotos(position);
            }
        });
        mFolderRv.setLayoutManager(new LinearLayoutManager(this));
        mFolderRv.addItemDecoration(new LineItemDecoration(this, LinearLayoutManager.HORIZONTAL, 1, mLineColor));
        mFolderRv.setAdapter(mFolderAdapter);
    }

    private void initPhotoRV() {
        mPicAdapter = new PhotoPickerAdapter(mPhotoRv);
        mPicAdapter.setOnItemChildClickListener(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, SPAN_COUNT, LinearLayoutManager.VERTICAL, false);
        mPhotoRv.setLayoutManager(layoutManager);
        mPhotoRv.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.photo_picker_divider)));
        mPhotoRv.setAdapter(mPicAdapter);
        if (getIntent().getBooleanExtra(EXTRA_PAUSE_ON_SCROLL, false)) {
            mPhotoRv.addOnScrollListener(new RVOnScrollListener(this));
        }
        ArrayList<String> selectedImages = getIntent().getStringArrayListExtra(EXTRA_SELECTED_IMAGES);
        if (selectedImages != null && selectedImages.size() > mMaxChooseCount) {
            String selectedPhoto = selectedImages.get(0);
            selectedImages.clear();
            selectedImages.add(selectedPhoto);
        }
        mPicAdapter.setSelectedImages(selectedImages);
    }

    private void renderTopRightBtn() {
        if (mPicAdapter.getSelectedCount() == 0) {
            mRightTv.setEnabled(false);
            mRightTv.setText(R.string.ok);
        } else {
            mRightTv.setEnabled(true);
            mRightTv.setText(getString(R.string.format_photo_ok, mPicAdapter.getSelectedCount() + "/" + mMaxChooseCount));
        }
    }

    private void requestStoragePermission() {
        AndPermission.with(this)
                .requestCode(200)
                .permission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 200) {
                            showLoadingDialog();
                            startLoad();
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (requestCode == 200) {
                            AndPermission.defaultSettingDialog(PhotoPickerActivity.this)
                                    .setMessage(getString(R.string.format_permission_error, mPermissionStorage)).show();
                        }
                    }
                }).start();
    }

    private void startLoad() {
        mLoadPhotoTask = new LoadPhotoTask(this, this, mTakePhotoEnabled).perform();
    }

    private void showLoadingDialog() {
        if (mLoadingPb != null) {
            mLoadingPb.setVisibility(View.VISIBLE);
        }
    }

    private void dismissLoadingDialog() {
        if (mLoadingPb != null) {
            mLoadingPb.setVisibility(View.GONE);
        }
    }

    private void toastMaxCountTip() {
        ToastUtils.showShortToast(getString(R.string.format_photo_picker_max, mMaxChooseCount));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
                ArrayList<String> selectedImages = mPicAdapter.getSelectedImages();
                if (selectedImages == null) {
                    selectedImages = new ArrayList<>();
                }
                String imagePath = mImageCaptureManager.getCurrentPhotoPath();
                selectedImages.add(imagePath);

                mImageCaptureManager.refreshGallery();
                //因为广播扫描媒体库，并不能及时更新数据库，所以加了个缓存
                if (!TakePhotoCache.mCache.contains(imagePath)) {
                    TakePhotoCache.mCache.add(imagePath);
                }
                returnSelectedImages(selectedImages);
            } else if (requestCode == REQUEST_CODE_PREVIEW) {
                returnSelectedImages(PhotoPickerPreviewActivity.getSelectedImages(data));
            }
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_PREVIEW) {
            if (PhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                // 从拍照预览界面返回，删除之前拍的照片
                mImageCaptureManager.deletePhotoFile();
            } else {
                mPicAdapter.setSelectedImages(PhotoPickerPreviewActivity.getSelectedImages(data));
                renderTopRightBtn();
            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mTakePhotoEnabled) {
            mImageCaptureManager.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onItemChildClick(ViewGroup viewGroup, View view, int position) {
        if (view.getId() == R.id.item_photo_camera_camera_iv) {
            handleTakePhoto();
        } else if (view.getId() == R.id.item_photo_picker_photo_iv) {
            changeToPreview(position);
        } else if (view.getId() == R.id.item_photo_picker_flag_iv) {
            handleClickSelectFlagIv(position);
        }
    }

    /**
     * 处理拍照
     */
    private void handleTakePhoto() {
        if (mMaxChooseCount == 1) {
            // 单选
            takePhoto();
        } else if (mPicAdapter.getSelectedCount() == mMaxChooseCount) {
            toastMaxCountTip();
        } else {
            takePhoto();
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        if (mPicAdapter.getSelectedCount() >= mMaxChooseCount) {
            toastMaxCountTip();
            return;
        }

        AndPermission.with(this)
                .requestCode(100)
                .permission(Manifest.permission.CAMERA)
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 100) {
                            try {
                                startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
                            } catch (IOException e) {
                                ToastUtils.showShortToast(R.string.tip_unkown_error);
                            }
                        }
                    }

                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (requestCode == 100) {
                            AndPermission.defaultSettingDialog(PhotoPickerActivity.this)
                                    .setMessage(getString(R.string.format_permission_error, mPermissionCamera)).show();
                        }
                    }
                }).start();
    }

    /**
     * 跳转到图片选择预览界面
     *
     * @param position 当前点击的item的索引位置
     */
    private void changeToPreview(int position) {
        int currentPosition = position;
        if (mCurrentImageFolderModel.isTakePhotoEnabled()) {
            currentPosition--;
        }
        PhotoPickerPreviewActivity.startForResult(this, mMaxChooseCount,
                mPicAdapter.getSelectedImages(), (ArrayList<String>) mPicAdapter.getData(), currentPosition, false,
                REQUEST_CODE_PREVIEW);
    }

    /**
     * 处理点击选择按钮事件
     *
     * @param position 当前点击的item的索引位置
     */
    private void handleClickSelectFlagIv(int position) {
        String currentImage = mPicAdapter.getItem(position);
        if (mMaxChooseCount == 1) {
            // 单选
            if (mPicAdapter.getSelectedCount() > 0) {
                String selectedImage = mPicAdapter.getSelectedImages().remove(0);
                if (TextUtils.equals(selectedImage, currentImage)) {
                    mPicAdapter.notifyItemChanged(position);
                } else {
                    int preSelectedImagePosition = mPicAdapter.getData().indexOf(selectedImage);
                    mPicAdapter.notifyItemChanged(preSelectedImagePosition);
                    mPicAdapter.getSelectedImages().add(currentImage);
                    mPicAdapter.notifyItemChanged(position);
                }
            } else {
                mPicAdapter.getSelectedImages().add(currentImage);
                mPicAdapter.notifyItemChanged(position);
            }
            renderTopRightBtn();
        } else {
            // 多选
            if (!mPicAdapter.getSelectedImages().contains(currentImage) && mPicAdapter.getSelectedCount() == mMaxChooseCount) {
                toastMaxCountTip();
            } else {
                if (mPicAdapter.getSelectedImages().contains(currentImage)) {
                    mPicAdapter.getSelectedImages().remove(currentImage);
                } else {
                    mPicAdapter.getSelectedImages().add(currentImage);
                }
                mPicAdapter.notifyItemChanged(position);

                renderTopRightBtn();
            }
        }
    }

    private void reloadPhotos(int position) {
        if (position < mImageFolderModels.size()) {
            mCurrentImageFolderModel = mImageFolderModels.get(position);
            if (mPickerFolderTv != null) {
                mPickerFolderTv.setText(mCurrentImageFolderModel.name);
            }

            mPicAdapter.setImageFolderModel(mCurrentImageFolderModel);
            mFolderAdapter.setData(mImageFolderModels);
            if (mFolderRL.getVisibility() == View.VISIBLE) {
                hidePhotoFolderUI();
            }
        }
    }

    private void togglePhotoFolderUI() {
        if (mFolderRL.getVisibility() == View.VISIBLE) {
            hidePhotoFolderUI();
        } else {
            mFolderRL.setVisibility(View.VISIBLE);

            mFolderBack.clearAnimation();
            ViewCompat.setAlpha(mFolderBack, 0);
            ViewCompat.setTranslationY(mFolderRv, mFolderRv.getHeight());
            //设置null是必须的，否则调用之前设置的listener
            ViewCompat.animate(mFolderRv).translationY(0).setListener(null).setDuration(ANIM_DURATION).setInterpolator(new DecelerateInterpolator(2)).start();
            ViewCompat.animate(mFolderBack).alpha(1).setDuration(ANIM_DURATION).setInterpolator(new DecelerateInterpolator(2)).start();
        }
    }

    private void hidePhotoFolderUI() {
        ViewCompat.animate(mFolderRv).translationY(mFolderRv.getHeight()).setDuration(ANIM_DURATION).setListener(new ViewPropertyAnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(View view) {
                if (mFolderRL != null) {
                    mFolderRL.setVisibility(View.INVISIBLE);
                }
            }
        }).start();
        ViewCompat.setAlpha(mFolderBack, 1);
        ViewCompat.animate(mFolderBack).alpha(0).setDuration(ANIM_DURATION).start();
    }

    @Override
    public void onPostExecute(ArrayList<ImageFolderModel> imageFolderModels) {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
        mImageFolderModels = imageFolderModels;
        reloadPhotos(0);
    }

    @Override
    public void onTaskCancelled() {
        dismissLoadingDialog();
        mLoadPhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        dismissLoadingDialog();
        cancelLoadPhotoTask();
        super.onDestroy();
    }

    private void cancelLoadPhotoTask() {
        if (mLoadPhotoTask != null) {
            mLoadPhotoTask.cancelTask();
            mLoadPhotoTask = null;
        }
    }

    /**
     * 返回已选中的图片集合
     *
     * @param selectedImages
     */
    private void returnSelectedImages(ArrayList<String> selectedImages) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * @param activity       应用程序上下文
     * @param imageDir       拍照后图片保存的目录。如果传null表示没有拍照功能，如果不为null则具有拍照功能，
     * @param maxChooseCount 图片选择张数的最大值
     * @param selectedImages 当前已选中的图片路径集合，可以传null
     * @param pauseOnScroll  滚动列表时是否暂停加载图片
     * @param requestCode    请求码
     * @return
     */
    public static void startForResult(Activity activity,
                                      File imageDir,
                                      int maxChooseCount,
                                      ArrayList<String> selectedImages,
                                      boolean pauseOnScroll,
                                      int requestCode) {
        Intent intent = new Intent(activity, PhotoPickerActivity.class);
        intent.putExtra(EXTRA_IMAGE_DIR, imageDir);
        intent.putExtra(EXTRA_MAX_CHOOSE_COUNT, maxChooseCount);
        intent.putStringArrayListExtra(EXTRA_SELECTED_IMAGES, selectedImages);
        intent.putExtra(EXTRA_PAUSE_ON_SCROLL, pauseOnScroll);
        activity.startActivityForResult(intent, requestCode);
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

}