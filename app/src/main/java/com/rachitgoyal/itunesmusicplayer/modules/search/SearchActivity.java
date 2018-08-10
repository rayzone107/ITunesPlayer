package com.rachitgoyal.itunesmusicplayer.modules.search;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.modules.base.BaseActivity;
import com.rachitgoyal.itunesmusicplayer.modules.list.ListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.search_cv)
    CardView mCardView;

    @BindView(R.id.logo_iv)
    ImageView mLogoIV;

    @BindView(R.id.title_tv)
    TextView mTitleTV;

    private SearchPresenter mPresenter;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        mPresenter = new SearchPresenter(this, new SearchDataManager(new SearchServiceImpl(mContext)));

        mCardView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Intent intent = new Intent(SearchActivity.this, ListActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SearchActivity.this,
                        mLogoIV, ViewCompat.getTransitionName(mLogoIV));
                startActivity(intent, options.toBundle());
                return false;
            }
        });
    }
}
