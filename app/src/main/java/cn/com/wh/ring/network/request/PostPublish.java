package cn.com.wh.ring.network.request;

import java.util.List;

/**
 * Created by Hui on 2017/8/17.
 */
public class PostPublish {
    private String description;
    private List<String> mediaContent;
    private String addressCode;
    private int postType;
    private boolean anonymous;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMediaContent() {
        return mediaContent;
    }

    public void setMediaContent(List<String> mediaContent) {
        this.mediaContent = mediaContent;
    }

    public String getAddressCode() {
        return addressCode;
    }

    public void setAddressCode(String addressCode) {
        this.addressCode = addressCode;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }
}
