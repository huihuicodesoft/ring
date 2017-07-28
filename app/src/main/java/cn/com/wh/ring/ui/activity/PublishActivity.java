package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.photo.photopicker.widget.PTSortableNinePhotoLayout;
import cn.com.wh.ring.R;
import cn.com.wh.ring.utils.InputMethodUtils;

/**
 * Created by Hui on 2017/7/27.
 */

public class PublishActivity extends TitleActivity implements PTSortableNinePhotoLayout.Delegate, InputMethodUtils.OnKeyBoardChangeListener {
    private static final int REQUEST_CODE_SELECT_PHOTO = 0X23;
    private static final int REQUEST_CODE_PREVIEW_PHOTO = 0X24;
    private static final int MAX_CONTENT_LENGTH = 300;

    @BindView(R.id.root_publish_rl)
    RelativeLayout mRootPublishRl;
    @BindView(R.id.remain_word_tv)
    TextView mRemainWordTv;
    @BindView(R.id.content_et)
    EditText mContentEt;
    @BindView(R.id.ptSortableNinePhotoLayout)
    PTSortableNinePhotoLayout mPTSortableNinePhotoLayout;
    @BindView(R.id.anonymous_iv)
    ImageView mAnonymousIv;

    InputMethodUtils mInputMethodUtils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        unbinder = ButterKnife.bind(this);

        mTitleTv.setText(R.string.publish_post);
        mRightTv.setText(R.string.publish);
        mContentEt.setFilters(new InputFilter[] { new InputFilter.LengthFilter(MAX_CONTENT_LENGTH)});
        mRemainWordTv.setText(getResources().getString(R.string.format_remain_word, MAX_CONTENT_LENGTH));

        mInputMethodUtils = new InputMethodUtils();
        mInputMethodUtils.onCreated(this);
        mInputMethodUtils.addKeyBoardChangeListener(this);

        mPTSortableNinePhotoLayout.setMaxItemCount(9);
        mPTSortableNinePhotoLayout.setDelegate(this);
        mPTSortableNinePhotoLayout.setPlusEnable(false);
    }

    @OnClick(R.id.publish_content_ll)
    void onPanel() {
        if (mInputMethodUtils != null)
            mInputMethodUtils.showKeyBoardState(mContentEt);
    }

    @OnClick(R.id.photo_tv)
    void onPhoto() {
        ArrayList<String> selectedImages = mPTSortableNinePhotoLayout.getData();
        PhotoPickerActivity.startForResult(this, new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto"),
                mPTSortableNinePhotoLayout.getMaxItemCount(), selectedImages, true, REQUEST_CODE_SELECT_PHOTO);
    }

    @OnClick(R.id.anonymous_ll)
    void onAnonymous() {
        mAnonymousIv.setSelected(!mAnonymousIv.isSelected());
    }

    @OnTextChanged(R.id.content_et)
    void onContentTextChange(CharSequence text) {
        if (!TextUtils.isEmpty(text))
            mRemainWordTv.setText(getResources().getString(R.string.format_remain_word, MAX_CONTENT_LENGTH - text.length()));
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
        PhotoPickerPreviewActivity.startForResult(this, sortableNinePhotoLayout.getMaxItemCount(), selectedImages, selectedImages, position, false, REQUEST_CODE_PREVIEW_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PHOTO) {
                mPTSortableNinePhotoLayout.setData(PhotoPickerActivity.getSelectedImages(data));
            } else if (requestCode == REQUEST_CODE_PREVIEW_PHOTO) {
                mPTSortableNinePhotoLayout.setData(PhotoPickerPreviewActivity.getSelectedImages(data));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mInputMethodUtils != null)
            mInputMethodUtils.hideKeyBoardState(mContentEt, false);
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
