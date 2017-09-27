package cn.com.wh.ring.ui.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.com.wh.ring.R;

/**
 * Created by Hui on 2017/9/20.
 */

public class ProgressDialog extends android.app.ProgressDialog {
    private int messageResId;

    public ProgressDialog(Context context) {
        this(context, R.style.ProgressDialog);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.dialog_progress);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        TextView messageTv = (TextView) findViewById(R.id.dialog_loading_message_tv);
        if (messageResId > 0) {
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText(messageResId);
        } else {
            messageTv.setVisibility(View.GONE);
        }
    }

    public void setMessage(@StringRes int resId) {
        messageResId = resId;
    }

}