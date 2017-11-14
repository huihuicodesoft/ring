package cn.com.wh.ring.ui.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.wh.photo.photopicker.decoration.LineItemDecoration;
import cn.com.wh.ring.R;
import cn.com.wh.ring.network.response.BannerItem;
import cn.com.wh.ring.network.response.Help;
import cn.com.wh.ring.ui.adapter.HelpAdapter;
import cn.com.wh.ring.ui.fragment.base.ButterKnifeFragment;
import cn.com.wh.ring.ui.view.ListSwipeRefreshLayout;

/**
 * Created by Hui on 2017/7/13.
 */

public class MainFindFragment extends ButterKnifeFragment {
    @BindView(R.id.listSwipeRefreshLayout)
    ListSwipeRefreshLayout mListSwipeRefreshLayout;

    @BindDimen(R.dimen.height_line_division_horizontal)
    int divisionLineHeight;
    @BindColor(R.color.line_division)
    int divisionLineColor;

    HelpAdapter mAdapter;
    List<BannerItem> mBannerItems = new ArrayList<>();
    List<Help> mHelps = new ArrayList<>();

    public static String[] titles = new String[]{
            "伪装者:胡歌演绎'痞子特工'",
            "无心法师:生死离别!月牙遭虐杀",
            "花千骨:尊上沦为花千骨",
            "综艺饭:胖轩偷看夏天洗澡掀波澜",
            "碟中谍4:阿汤哥高塔命悬一线,超越不可能",
    };
    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_find, container, false);
        unbinder = ButterKnife.bind(this, root);
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mListSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HelpAdapter(mBannerItems, mHelps);
        mListSwipeRefreshLayout.setAdapter(mAdapter);
        LineItemDecoration lineItemDecoration = new LineItemDecoration(getContext(), LinearLayoutManager.HORIZONTAL,
                divisionLineHeight, divisionLineColor);
        mListSwipeRefreshLayout.addItemDecoration(lineItemDecoration);

        for (int i = 0; i < titles.length; i++) {
            BannerItem item = new BannerItem();
            item.setImageUrl(urls[i]);
            item.setText(titles[i]);
            mBannerItems.add(item);
        }
        for (int i = 0; i < 10; i++) {
            mHelps.add(new Help());
        }
        mAdapter.notifyDataSetChanged();
    }
}