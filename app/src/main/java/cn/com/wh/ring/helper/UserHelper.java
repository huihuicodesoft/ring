package cn.com.wh.ring.helper;

import android.widget.ImageView;

import cn.com.wh.photo.GlideApp;

/**
 * Created by Hui on 2017/9/27.
 */

public class UserHelper {
    public static void loadAvatar(ImageView avatarIv, String url) {
        GlideApp.with(avatarIv.getContext()).load(url).circleCrop().into(avatarIv);
    }
}
