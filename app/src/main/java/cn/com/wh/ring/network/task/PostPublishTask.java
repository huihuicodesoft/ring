package cn.com.wh.ring.network.task;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.wh.ring.event.PostPublishEvent;
import cn.com.wh.ring.helper.RequestHelper;
import cn.com.wh.ring.network.response.Post;
import cn.com.wh.ring.network.response.PostType;
import cn.com.wh.ring.network.response.Response;
import cn.com.wh.ring.network.retrofit.ReturnCode;
import cn.com.wh.ring.network.service.Services;
import cn.com.wh.ring.utils.LogUtils;
import okhttp3.MultipartBody;
import retrofit2.Call;

/**
 * Created by Hui on 2017/8/18.
 */

public class PostPublishTask extends Thread {
    private static final String TAG = PostPublishTask.class.getName();

    private Post mPost;


    public PostPublishTask(Post post) {
        super();
        this.mPost = post;
    }

    @Override
    public void run() {
        if (mPost == null)
            return;

        List<String> mediaList = mPost.getMediaList();
        if (mediaList == null || mediaList.isEmpty()) {
            postPublish(null);
        } else {
            List<String> names = uploadFiles(mediaList);
            if (names != null && !names.isEmpty()) {
                postPublish(names);
            }
        }
    }

    private List<String> uploadFiles(List<String> fileList) {
        List<String> names = new ArrayList<>();
        for (String file : fileList) {
            String name = uploadFile(file);
            if (TextUtils.isEmpty(name)) {
                names = null;
                break;
            } else {
                names.add(name);
            }
        }
        return names;
    }

    private String uploadFile(String file) {
        String name = null;
        try {
            MultipartBody body = RequestHelper.filesToMultipartBody(Arrays.asList(file));
            Call<Response<List<String>>> fileCall = Services.fileService.upload(body);
            retrofit2.Response<Response<List<String>>> response = fileCall.execute();
            if (response.isSuccessful()) {
                Response<List<String>> temp = response.body();
                if (temp != null && temp.getCode() == ReturnCode.OK) {
                    name = temp.getPayload().get(0);
                }
            }
        } catch (Exception e) {
            LogUtils.logD(TAG, e.toString());
        } finally {
            return name;
        }
    }

    private void postPublish(List<String> mediaContent) {
        cn.com.wh.ring.network.request.PostPublish postPublish = new cn.com.wh.ring.network.request.PostPublish();
        postPublish.setDescription(mPost.getDescription());
        if (mediaContent != null)
            postPublish.setMediaContent(mediaContent);
        postPublish.setAnonymous(mPost.isAnonymous());
        postPublish.setAddressCode(mPost.getAddressCode());

        PostType postType = mPost.getPostType();
        if (postType != null)
            postPublish.setPostType(postType.getId());

        try {
            Call<Response<Object>> call = Services.postService.publish(postPublish);
            retrofit2.Response<Response<Object>> response = call.execute();
            if (response.isSuccessful()) {
                Response<Object> temp = response.body();
                if (temp != null && temp.getCode() == ReturnCode.OK) {
                    //发布成功
                    EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_REQUEST_SUCCESS, mPost.getId()));
                } else {
                    //发布失败
                    EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_REQUEST_FAIL, mPost.getId()));
                }
            } else {
                //发布失败
                EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_REQUEST_FAIL, mPost.getId()));
            }
        } catch (Exception e) {
            //发布失败
            EventBus.getDefault().post(new PostPublishEvent(PostPublishEvent.TYPE_REQUEST_FAIL, mPost.getId()));
        }
    }
}
