package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.SearchHistoryItem;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BaseActivity;
import com.rachitgoyal.itunesmusicplayer.modules.favourites.FavouritesActivity;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActivity extends BaseActivity implements ListContract.View, SearchViewHolder.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.search_sv)
    SearchView mSearchView;

    @BindView(R.id.search_rv)
    RecyclerView mSearchRV;

    @BindView(R.id.song_count_tv)
    TextView mSongCountTV;

    @BindView(R.id.loading_lav)
    LottieAnimationView mLoadingLAV;

    @BindView(R.id.error_layout)
    View mErrorLayout;

    @BindView(R.id.error_lav)
    LottieAnimationView mErrorLav;

    @BindView(R.id.error_title_tv)
    TextView mErrorTitleTV;

    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTV;

    @BindView(R.id.error_button)
    Button mErrorButton;

    private ListContract.Presenter mPresenter;
    private SongListPagerAdapter mSongListPagerAdapter;
    private ArrayList<Song> mSongs = new ArrayList<>();

    private SearchAdapter mSearchAdapter;
    private List<SearchHistoryItem> mSearchHistoryItems = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        mPresenter = new ListPresenter(this, new ListDataManager(new ListServiceImpl(this)));

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchRV.setVisibility(View.GONE);
                SearchHistoryItem searchHistoryItem = Select.from(SearchHistoryItem.class).where(Condition.prop(SearchHistoryItem.QUERY_STRING).eq(query)).first();
                if (searchHistoryItem == null) {
                    searchHistoryItem = new SearchHistoryItem(query, new Date());
                }
                searchHistoryItem.save();
                mPresenter.fetchSongs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.isEmpty()) {
                    mSearchRV.setVisibility(View.GONE);
                } else {
                    mSearchHistoryItems = Select.from(SearchHistoryItem.class).where(Condition.prop(SearchHistoryItem.QUERY_STRING).like(s + "%")).limit("5").list();
                    if (mSearchHistoryItems.isEmpty()) {
                        mSearchRV.setVisibility(View.GONE);
                    } else {
                        mSearchRV.setVisibility(View.VISIBLE);
                        mSearchAdapter.clear();
                        mSearchAdapter.addQueries(mSearchHistoryItems);
                        mSearchAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        setSupportActionBar(mToolbar);

        mSearchAdapter = new SearchAdapter(mSearchHistoryItems, this);
        mSearchRV.setLayoutManager(new LinearLayoutManager(mContext));
        mSearchRV.setAdapter(mSearchAdapter);

        mSongListPagerAdapter = new SongListPagerAdapter(getSupportFragmentManager(), mSongs);
        mViewPager.setAdapter(mSongListPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mPresenter.fetchRecentSongs();

        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSearchRV.setVisibility(View.GONE);
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSongListPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void changeViewState(@ViewState.ItemTypeDef int viewState) {
        switch (viewState) {
            case ViewState.LOADING:
                mLoadingLAV.setVisibility(View.VISIBLE);
                showData(false);
                mErrorLayout.setVisibility(View.GONE);
                break;
            case ViewState.ERROR:
                mLoadingLAV.setVisibility(View.GONE);
                showData(false);
                mErrorLayout.setVisibility(View.VISIBLE);
                break;
            case ViewState.DATA:
                mLoadingLAV.setVisibility(View.GONE);
                showData(true);
                mErrorLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void showData(boolean show) {
        mViewPager.setVisibility(show ? View.VISIBLE : View.GONE);
        mTabLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mSongCountTV.setVisibility(show ? View.VISIBLE : View.GONE);
    }


    @OnClick(R.id.favourite_iv)
    public void onFavouriteClick(View view) {
        Intent intent = new Intent(ListActivity.this, FavouritesActivity.class);
        startActivity(intent);
    }

    @Override
    public void showSongsList(boolean isRecent, ArrayList<Song> songs) {
        mSongs.clear();
        mSongs = songs;
        mSongCountTV.setText(getString(isRecent ? R.string.recent_songs : R.string.all_songs, songs.size()));
        mSongListPagerAdapter.clear();
        mSongListPagerAdapter.updateSongs(songs);
        mSongListPagerAdapter.notifyDataSetChanged();
        if (!isRecent) {
            mViewPager.setCurrentItem(mSongListPagerAdapter.getCount() - 1);
        }
    }

    @OnClick(R.id.error_button)
    public void onRetryClick() {
        mSongListPagerAdapter.clear();
        mPresenter.retry(mSearchView.getQuery().toString());
    }

    @Override
    public void showError(ErrorState errorState) {
        mErrorLav.setAnimation(errorState.getErrorLottieResId());
        mErrorLav.playAnimation();
        mErrorTitleTV.setText(errorState.getErrorTitleResId());
        mErrorMessageTV.setText(errorState.getErrorMessageResId());
        mErrorButton.setVisibility(errorState.isShowRetry() ? View.VISIBLE : View.GONE);
        mErrorButton.setText(errorState.getRetryTextResId());
    }

    @Override
    public void onItemClick(SearchHistoryItem searchHistoryItem, int position) {
        mSearchRV.setVisibility(View.GONE);
        mSearchView.setQuery(searchHistoryItem.getQueryString(), true);
    }

    @Override
    public void onBackPressed() {
        if (mSearchRV.getVisibility() == View.VISIBLE) {
            mSearchRV.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
