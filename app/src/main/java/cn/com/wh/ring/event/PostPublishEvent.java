package cn.com.wh.ring.event;

/**
 * Created by Hui on 2017/8/17.
 */

public class PostPublishEvent {
    public static final int TYPE_SKIP_ME = 1;
    public static final int TYPE_SKIP_POST = 2;
    public static final int TYPE_REQUEST_FAIL = 3;
    public static final int TYPE_REQUEST_SUCCESS = 4;

    public final int type;
    public final Long id;

    /**
     * @param type
     * 1：是跳转到个人页面
     * 2: 是跳转个人页面的发帖页面
     * 3. 发表失败
     * 4. 发表成功
     * @param id
     */
    public PostPublishEvent(int type, Long id) {
        this.type = type;
        this.id = id;
    }
}
