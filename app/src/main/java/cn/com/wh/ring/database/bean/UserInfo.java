package cn.com.wh.ring.database.bean;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "USER_INFO".
 */
@Entity
public class UserInfo {
    public static final int SEX_MAN = 1;
    public static final int SEX_WOMAN = 2;
    public static final int SEX_UN_SELECT = 3;

    @Id(autoincrement = true)
    private Long id;

    @Unique
    private Long userId;
    private String nickname;
    private Long birthday;
    private Integer sex;
    private String avatar;
    private String signature;
    private String region;
    private Long lastModifiedTime;

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public UserInfo(Long id) {
        this.id = id;
    }

    @Generated(hash = 951725279)
    public UserInfo(Long id, Long userId, String nickname, Long birthday, Integer sex, String avatar, String signature, String region, Long lastModifiedTime) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.birthday = birthday;
        this.sex = sex;
        this.avatar = avatar;
        this.signature = signature;
        this.region = region;
        this.lastModifiedTime = lastModifiedTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getBirthday() {
        return birthday;
    }

    public void setBirthday(Long birthday) {
        this.birthday = birthday;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

}
