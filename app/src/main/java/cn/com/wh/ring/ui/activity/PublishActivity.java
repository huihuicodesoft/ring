package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.com.wh.photo.photopicker.util.FileTypeUtils;
import cn.com.wh.photo.photopicker.widget.PTSortableNinePhotoLayout;
import cn.com.wh.ring.MainApplication;
import cn.com.wh.ring.R;
import cn.com.wh.ring.database.bean.PostPublish;
import cn.com.wh.ring.database.dao.PostPublishDao;
import cn.com.wh.ring.database.sp.DataCenter;
import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.ui.activity.base.TitleActivity;
import cn.com.wh.ring.utils.InputMethodUtils;
import cn.com.wh.ring.utils.ToastUtils;

/**
 * Created by Hui on 2017/7/27.
 */

public class PublishActivity extends TitleActivity implements PTSortableNinePhotoLayout.Delegate, InputMethodUtils.OnKeyBoardChangeListener {
    private static final int REQUEST_CODE_SELECT_PHOTO = 0X23;
    private static final int REQUEST_CODE_PREVIEW_PHOTO = 0X24;
    private static final int REQUEST_CODE_SELECT_TYPE = 0x19;
    private static final int MAX_CONTENT_LENGTH = 300;

    private static final int FILE_STATE_EMPTY = 0x5; //没有选择媒体资源
    private static final int FILE_STATE_IMAGE = 0x9; //选择媒体资源只有图片
    private static final int FILE_STATE_GIF = 0x7; //选择媒体资源只有GIF
    private static final int FILE_STATE_IMAGE_GIF = 0x97; //选择媒体资源只有图片和GIF
    private static final int FILE_STATE_VIDEO = 0x22; //选择媒体资源只有视频
    private static final int FILE_STATE_UNKOWN = 0x21; //选择媒体资源 不支持格式

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
    @BindView(R.id.select_type_tv)
    TextView mSelectTypeTv;

    InputMethodUtils mInputMethodUtils;
    private PostType mPostType;


    private View.OnClickListener mPublishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (interceptByType()) return;
            long id = insertDatabase();
            finish();
            EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_SKIP_ME, id));
        }
    };

    private long insertDatabase() {
        PostPublishDao postPublishDao = MainApplication.getInstance().getDaoSession().getPostPublishDao();
        PostPublish postPublish = new PostPublish();
        postPublish.setToken(DataCenter.getInstance().getToken());
        postPublish.setContent(getContent());

        List<String> list = mPTSortableNinePhotoLayout.getData();
        postPublish.setMediaContent(new Gson().toJson(list));
        postPublish.setType(new Gson().toJson(mPostType));
        postPublish.setTime(System.currentTimeMillis());
        postPublish.setAnonymous(mAnonymousIv.isSelected());
        postPublish.setState(PostPublish.STATE_PUBLISHING);

        return postPublishDao.insert(postPublish);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        unbinder = ButterKnife.bind(this);

        mContentEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_CONTENT_LENGTH)});
        mRemainWordTv.setText(getResources().getString(R.string.format_remain_word, MAX_CONTENT_LENGTH));

        mInputMethodUtils = new InputMethodUtils();
        mInputMethodUtils.onCreated(this);
        mInputMethodUtils.addKeyBoardChangeListener(this);

        mPTSortableNinePhotoLayout.setMaxItemCount(9);
        mPTSortableNinePhotoLayout.setDelegate(this);
        mPTSortableNinePhotoLayout.setPlusEnable(false);

        mRightTv.setText(R.string.publish);
        mRightTv.setOnClickListener(mPublishListener);

    }

    @OnClick(R.id.publish_content_ll)
    void onPanel() {
        if (mInputMethodUtils != null)
            mInputMethodUtils.showKeyBoardState(mContentEt);
    }

    @OnClick(R.id.select_type_ll)
    void onPostType() {
        SelectPostTypeActivity.startForResult(this, REQUEST_CODE_SELECT_TYPE);
    }

    @OnClick(R.id.photo_tv)
    void onPhoto() {
        ArrayList<String> selectedImages = mPTSortableNinePhotoLayout.getData();
        PhotoPickerActivity.startMultipleForResult(this, new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto"),
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
            } else if (requestCode == REQUEST_CODE_SELECT_TYPE) {
                mPostType = SelectPostTypeActivity.getIntentData(data);
                if (mPostType != null)
                    mSelectTypeTv.setText(mPostType.getName());
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
        mInputMethodUtils.onDestroy();
        super.onDestroy();
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

    @Override
    public void onBackPressed() {
        if (mContentEt != null) {
            String content = mContentEt.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                showEditDialog();
                return;
            }
        }
        if (mPTSortableNinePhotoLayout != null) {
            List<String> list = mPTSortableNinePhotoLayout.getData();
            if (list != null && !list.isEmpty()) {
                showEditDialog();
                return;
            }
        }
        super.onBackPressed();
    }

    private void showEditDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.tip)
                .content(R.string.dialog_abandon_edit)
                .positiveText(R.string.cancel)
                .negativeText(R.string.abandon)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                })
                .show();
    }

    private String getContent() {
        Editable editable = mContentEt.getText();
        return editable == null ? "" : editable.toString();
    }

    /**
     * 返回媒体组件状态值
     *
     * @return
     */
    private int getFileState() {
        int type = FILE_STATE_EMPTY;
        List<String> list = mPTSortableNinePhotoLayout.getData();
        if (list != null && !list.isEmpty()) {
            for (String path : list) {
                if (FileTypeUtils.isPhoto(path)) {
                    if (type == FILE_STATE_GIF) {
                        type = FILE_STATE_IMAGE_GIF;
                        break;
                    } else {
                        type = FILE_STATE_IMAGE;
                    }
                } else if (FileTypeUtils.isGif(path)) {
                    if (type == FILE_STATE_IMAGE) {
                        type = FILE_STATE_IMAGE_GIF;
                        break;
                    } else {
                        type = FILE_STATE_GIF;
                    }
                } else if (FileTypeUtils.isVideo(path)) {
                    type = FILE_STATE_VIDEO;
                    break;
                } else {
                    type = FILE_STATE_UNKOWN;
                }
            }
        }
        return type;
    }


    private boolean interceptByType() {
        if (mPostType == null) {
            ToastUtils.showShortToast("请选择吧");
            return true;
        } else {
            int supportType = mPostType.getSupport();
            int fileState = getFileState();

            if (fileState == FILE_STATE_UNKOWN) {
                ToastUtils.showShortToast("文件格式不支持");
            }

            if (supportType == PostType.SUPPORT_W) {
                if (TextUtils.isEmpty(getContent())) {
                    ToastUtils.showShortToast("内容不能为空");
                    return true;
                } else {
                    if (fileState != FILE_STATE_EMPTY) {
                        ToastUtils.showShortToast("该吧只支持文字");
                        return true;
                    }
                }
            } else if (supportType == PostType.SUPPORT_P) {
                if (fileState != FILE_STATE_IMAGE) {
                    ToastUtils.showShortToast("该吧只支持图片");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_V) {
                if (fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持视频文件");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_G) {
                if (fileState != FILE_STATE_GIF) {
                    ToastUtils.showShortToast("该吧只支持gif图");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_WP) {
                if (fileState == FILE_STATE_EMPTY) {
                    if (TextUtils.isEmpty(getContent())) {
                        ToastUtils.showShortToast("内容不能为空");
                        return true;
                    }
                } else if (fileState != FILE_STATE_IMAGE) {
                    ToastUtils.showShortToast("该吧只支持文字，图片");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_WV) {
                if (fileState == FILE_STATE_EMPTY) {
                    if (TextUtils.isEmpty(getContent())) {
                        ToastUtils.showShortToast("内容不能为空");
                        return true;
                    }
                } else if (fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持文字，视频");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_WG) {
                if (fileState == FILE_STATE_EMPTY) {
                    if (TextUtils.isEmpty(getContent())) {
                        ToastUtils.showShortToast("内容不能为空");
                        return true;
                    }
                } else if (fileState != FILE_STATE_GIF) {
                    ToastUtils.showShortToast("该吧只支持文字，gif");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_PV) {
                if (fileState != FILE_STATE_IMAGE && fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持图片，视频");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_PG) {
                if (fileState != FILE_STATE_IMAGE && fileState != FILE_STATE_GIF && fileState != FILE_STATE_IMAGE_GIF) {
                    ToastUtils.showShortToast("该吧只支持图片，gif");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_VG) {
                if (fileState != FILE_STATE_GIF && fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持gif，视频");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_WPV) {
                if (fileState == FILE_STATE_EMPTY) {
                    if (TextUtils.isEmpty(getContent())) {
                        ToastUtils.showShortToast("内容不能为空");
                        return true;
                    }
                } else if (fileState != FILE_STATE_IMAGE && fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持文字，图片，视频");
                    return true;
                }
            } else if (supportType == PostType.SUPPORT_PVG) {
                if (fileState != FILE_STATE_IMAGE && fileState != FILE_STATE_GIF && fileState != FILE_STATE_IMAGE_GIF && fileState != FILE_STATE_VIDEO) {
                    ToastUtils.showShortToast("该吧只支持图片，视频，gif");
                    return true;
                }
            }
        }
        return false;
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
    }

}
