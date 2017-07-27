package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.photo.photopicker.adapter.PhotoPageAdapter;
import cn.com.wh.photo.photopicker.imageloader.Image;
import cn.com.wh.photo.photopicker.imageloader.ImageLoader;
import cn.com.wh.photo.photopicker.task.PTAsyncTask;
import cn.com.wh.photo.photopicker.task.SavePhotoTask;
import cn.com.wh.photo.photopicker.util.PhotoPickerUtil;
import cn.com.wh.photo.photopicker.widget.HackyViewPager;
import cn.com.wh.photo.photoview.PhotoViewAttacher;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/24 下午2:59
 * 描述:图片预览界面
 */
public class PhotoPreviewActivity extends FullScreenActivity implements PhotoViewAttacher.OnViewTapListener, PTAsyncTask.Callback<Void> {
    private static final String EXTRA_SAVE_IMG_DIR = "EXTRA_SAVE_IMG_DIR";
    private static final String EXTRA_PREVIEW_IMAGES = "EXTRA_PREVIEW_IMAGES";
    private static final String EXTRA_CURRENT_POSITION = "EXTRA_CURRENT_POSITION";
    private static final String EXTRA_IS_SINGLE_PREVIEW = "EXTRA_IS_SINGLE_PREVIEW";
    private static final String EXTRA_PHOTO_PATH = "EXTRA_PHOTO_PATH";

    @BindView(R.id.photo_preview_content_hvp)
    HackyViewPager mContentHvp;

    private PhotoPageAdapter mPhotoPageAdapter;

    private boolean mIsSinglePreview;

    private File mSaveImgDir;

    private boolean mIsHidden = false;
    private SavePhotoTask mSavePhotoTask;

    /**
     * 上一次标题栏显示或隐藏的时间戳
     */
    private long mLastShowHiddenTime;

    /**
     * 获取查看多张图片的intent
     *
     * @param context
     * @param saveImgDir      保存图片的目录，如果传null，则没有保存图片功能
     * @param previewImages   当前预览的图片目录里的图片路径集合
     * @param currentPosition 当前预览图片的位置
     * @return
     */
    public static Intent newIntent(Context context, File saveImgDir, ArrayList<String> previewImages, int currentPosition) {
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putStringArrayListExtra(EXTRA_PREVIEW_IMAGES, previewImages);
        intent.putExtra(EXTRA_CURRENT_POSITION, currentPosition);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        return intent;
    }

    /**
     * 获取查看单张图片的intent
     *
     * @param context
     * @param saveImgDir 保存图片的目录，如果传null，则没有保存图片功能
     * @param photoPath  图片路径
     * @return
     */
    public static Intent newIntent(Context context, File saveImgDir, String photoPath) {
        Intent intent = new Intent(context, PhotoPreviewActivity.class);
        intent.putExtra(EXTRA_SAVE_IMG_DIR, saveImgDir);
        intent.putExtra(EXTRA_PHOTO_PATH, photoPath);
        intent.putExtra(EXTRA_CURRENT_POSITION, 0);
        intent.putExtra(EXTRA_IS_SINGLE_PREVIEW, true);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_preview);
        unbinder = ButterKnife.bind(this);
        initListener();
        processLogic();
    }

    protected void initListener() {
        mContentHvp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
               //
            }
        });
    }

    protected void processLogic() {
        mSaveImgDir = (File) getIntent().getSerializableExtra(EXTRA_SAVE_IMG_DIR);
        if (mSaveImgDir != null && !mSaveImgDir.exists()) {
            mSaveImgDir.mkdirs();
        }

        ArrayList<String> previewImages = getIntent().getStringArrayListExtra(EXTRA_PREVIEW_IMAGES);

        mIsSinglePreview = getIntent().getBooleanExtra(EXTRA_IS_SINGLE_PREVIEW, false);
        if (mIsSinglePreview) {
            previewImages = new ArrayList<>();
            previewImages.add(getIntent().getStringExtra(EXTRA_PHOTO_PATH));
        }

        int currentPosition = getIntent().getIntExtra(EXTRA_CURRENT_POSITION, 0);

        mPhotoPageAdapter = new PhotoPageAdapter(this, this, previewImages);
        mContentHvp.setAdapter(mPhotoPageAdapter);
        mContentHvp.setCurrentItem(currentPosition);
    }

    @Override
    public void onViewTap(View view, float x, float y) {

    }

    private synchronized void savePic() {
        if (mSavePhotoTask != null) {
            return;
        }

        final String url = mPhotoPageAdapter.getItem(mContentHvp.getCurrentItem());
        File file;
        if (url.startsWith("file")) {
            file = new File(url.replace("file://", ""));
            if (file.exists()) {
                PhotoPickerUtil.showSafe(getString(R.string.format_save_img_success_folder, file.getParentFile().getAbsolutePath()));
                return;
            }
        }

        // 通过MD5加密url生成文件名，避免多次保存同一张图片
        file = new File(mSaveImgDir, PhotoPickerUtil.md5(url) + ".png");
        if (file.exists()) {
            PhotoPickerUtil.showSafe(getString(R.string.format_save_img_success_folder, mSaveImgDir.getAbsolutePath()));
            return;
        }

        mSavePhotoTask = new SavePhotoTask(this, this, file);
        Image.download(url, new ImageLoader.DownloadDelegate() {
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                mSavePhotoTask.setBitmapAndPerform(bitmap);
            }

            @Override
            public void onFailed(String url) {
                mSavePhotoTask = null;
                ToastUtils.showShortToast(R.string.save_img_failure);
            }
        });
    }

    @Override
    public void onPostExecute(Void aVoid) {
        mSavePhotoTask = null;
    }

    @Override
    public void onTaskCancelled() {
        mSavePhotoTask = null;
    }

    @Override
    protected void onDestroy() {
        if (mSavePhotoTask != null) {
            mSavePhotoTask.cancelTask();
            mSavePhotoTask = null;
        }
        super.onDestroy();
    }
}