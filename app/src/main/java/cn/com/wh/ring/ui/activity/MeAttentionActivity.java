package cn.com.wh.ring.ui.activity;

import android.content.Context;
import android.content.Intent;

import cn.com.wh.ring.ui.activity.base.TitleActivity;

/**
 * Created by Hui on 2017/9/13.
 */

public class MeAttentionActivity extends TitleActivity {

    public static void start(Context context) {
        Intent intent = new Intent(context, MeAttentionActivity.class);
        context.startActivity(intent);
    }
}
