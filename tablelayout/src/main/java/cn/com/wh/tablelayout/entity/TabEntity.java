package cn.com.wh.tablelayout.entity;

import android.support.annotation.DrawableRes;

public class TabEntity {
    private String tabTitle;
    private int tabSelectedIcon;
    private int tabUnselectedIcon;

    public TabEntity(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public TabEntity(String tabTitle, int tabSelectedIcon, int tabUnselectedIcon) {
        this.tabTitle = tabTitle;
        this.tabSelectedIcon = tabSelectedIcon;
        this.tabUnselectedIcon = tabUnselectedIcon;
    }

    public String getTabTitle(){
        return tabTitle;
    }

    @DrawableRes
    public int getTabSelectedIcon(){
        return tabSelectedIcon;
    }

    @DrawableRes
    public int getTabUnselectedIcon(){
        return tabUnselectedIcon;
    }
}