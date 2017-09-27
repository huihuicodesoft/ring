package cn.com.wh.photo.photopicker.model;

/**
 * Created by Hui on 2017/9/25.
 */

public enum  MODE_TYPE {
    SINGLE(12), MULTIPLE(13);

    private int index;

    MODE_TYPE(int idx) {
        this.index = idx;
    }

    public int getIndex() {
        return index;
    }
}
