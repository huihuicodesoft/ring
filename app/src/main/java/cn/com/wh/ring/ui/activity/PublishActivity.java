package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.wh.photo.photopicker.widget.PTSortableNinePhotoLayout;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.InputMethodUtils;

/**
 * Created by Hui on 2017/7/27.
 */

public class PublishActivity extends TitleActivity implements PTSortableNinePhotoLayout.Delegate, InputMethodUtils.OnKeyBoardChangeListener {
    private static final int REQUEST_CODE_SELECT_PHOTO = 0X23;
    private static final int REQUEST_CODE_PREVIEW_PHOTO = 0X24;

    @BindView(R.id.root_publish_rl)
    RelativeLayout mRootPublishRl;
    @BindView(R.id.content_et)
    EditText mContentEt;
    @BindView(R.id.ptSortableNinePhotoLayout)
    PTSortableNinePhotoLayout mPTSortableNinePhotoLayout;

    InputMethodUtils mInputMethodUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        unbinder = ButterKnife.bind(this);

        mTitleTv.setText(R.string.publish_post);
        mRightTv.setText(R.string.publish);

        mInputMethodUtils = new InputMethodUtils();
        mInputMethodUtils.onCreated(this);
        mInputMethodUtils.addKeyBoardChangeListener(this);

        mPTSortableNinePhotoLayout.setMaxItemCount(9);
        mPTSortableNinePhotoLayout.setDelegate(this);
        mPTSortableNinePhotoLayout.setPlusEnable(false);
    }

    @OnClick(R.id.publish_content_ll)
    void onContent() {
        mInputMethodUtils.showKeyBoardState(mContentEt);
    }

    @OnClick(R.id.photo_tv)
    void onPhoto() {
        ArrayList<String> selectedImages = mPTSortableNinePhotoLayout.getData();
        PhotoPickerActivity.startForResult(this, new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto"), 9, selectedImages, true, REQUEST_CODE_SELECT_PHOTO);
    }

    @Override
    public void onClickAddNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
    }

    @Override
    public void onClickDeleteNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPTSortableNinePhotoLayout.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(PTSortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        ArrayList<String> selectedImages = sortableNinePhotoLayout.getData();
        PhotoPickerPreviewActivity.startForResult(this, 9, selectedImages, selectedImages, position, false, REQUEST_CODE_PREVIEW_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
                mPTSortableNinePhotoLayout.setData(PhotoPickerActivity.getSelectedImages(data));
            } else if (requestCode == REQUEST_CODE_PREVIEW_PHOTO){
                mPTSortableNinePhotoLayout.setData(PhotoPickerPreviewActivity.getSelectedImages(data));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInputMethodUtils.onDestroy();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onChange(InputMethodUtils.KEY_BOARD_STATE keyBoardState, int keyBoardHeight, boolean isInitiative) {
        if (keyBoardState.equals(InputMethodUtils.KEY_BOARD_STATE.SYSTEM_SHOWED)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRootPublishRl.getLayoutParams();
            params.bottomMargin = keyBoardHeight;
            mRootPublishRl.setLayoutParams(params);

        }
        if (keyBoardState.equals(InputMethodUtils.KEY_BOARD_STATE.SYSTEM_HIDED)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRootPublishRl.getLayoutParams();
            params.bottomMargin = 0;
            mRootPublishRl.setLayoutParams(params);
        }
    }
}
