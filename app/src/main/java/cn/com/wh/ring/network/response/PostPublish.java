package cn.com.wh.ring.network.response;

import java.util.List;

/**
 * Created by Hui on 2017/12/1.
 */

public class PostPublish {
    /**
     * 帖子状态值
     * 1：审核中（默认值）
     * 2：审核失败
     * 3：审核成功
     */
    public static final byte STATE_CHECKING = 1;
    public static final byte STATE_CHECK_FAIL = 2;
    public static final byte STATE_CHECK_SUCCESS = 3;

    private Long id;
    private String uuid;
    private int state;
    private String description;
    private List<String> mediaList;
    private PostType postType;
    private Boolean anonymous;
    private String region;
    private int praiseNumber;
    private int criticizeNumber;
    private int commentNumber;
    private int reportNumber;
    private long creationTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<String> mediaList) {
        this.mediaList = mediaList;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public Boolean getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getPraiseNumber() {
        return praiseNumber;
    }

    public void setPraiseNumber(int praiseNumber) {
        this.praiseNumber = praiseNumber;
    }

    public int getCriticizeNumber() {
        return criticizeNumber;
    }

    public void setCriticizeNumber(int criticizeNumber) {
        this.criticizeNumber = criticizeNumber;
    }

    public int getCommentNumber() {
        return commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }

    public int getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(int reportNumber) {
        this.reportNumber = reportNumber;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }
}
