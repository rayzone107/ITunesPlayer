package com.rachitgoyal.itunesmusicplayer.modules.search;

import com.rachitgoyal.itunesmusicplayer.modules.base.BasePresenter;

class SearchPresenter extends BasePresenter implements SearchContract.Presenter {

    private SearchContract.View mView;
    private SearchDataManager mDataManager;

    SearchPresenter(SearchContract.View view, SearchDataManager searchDataManager) {
        mView = view;
        mDataManager = searchDataManager;
    }
}
