package cn.com.wh.ring.network.response;

import java.io.Serializable;

/**
 * Created by Hui on 2017/8/1.
 */

public class PostType implements Serializable {
    public static final int SUPPORT_ALL = 1; //全被格式
    public static final int SUPPORT_W = 2; //只支持文字
    public static final int SUPPORT_P = 3; //[文字] + 图片
    public static final int SUPPORT_V = 4; //[文字] + 视频
    public static final int SUPPORT_G = 5; //[文字] + gif
    public static final int SUPPORT_WP = 6; // 2/3
    public static final int SUPPORT_WV = 7; // 2/4
    public static final int SUPPORT_WG = 8;// 2/5
    public static final int SUPPORT_PV = 9;// 3/4
    public static final int SUPPORT_PG = 10;// 3/5
    public static final int SUPPORT_VG = 11;// 4/5
    public static final int SUPPORT_WPV = 12;// 2/3/4
    public static final int SUPPORT_PVG = 13;// 3/4/5

    private int id;
    private String name;
    private String description;
    private String symbol;
    private int support;
    private Long createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
