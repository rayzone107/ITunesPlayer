package com.rachitgoyal.itunesmusicplayer.modules.list;

import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.models.SongResponse;
import com.rachitgoyal.itunesmusicplayer.modules.base.BasePresenter;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.NoInternetException;
import com.rachitgoyal.itunesmusicplayer.utils.Utils;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListPresenter extends BasePresenter implements ListContract.Presenter {

    private ListContract.View mView;
    private ListDataManager mDataManager;

    ListPresenter(ListContract.View view, ListDataManager searchDataManager) {
        mView = view;
        mDataManager = searchDataManager;
    }

    @Override
    public void fetchRecentSongs() {
        ArrayList<Song> recentSongs = (ArrayList<Song>) Select.from(Song.class).list();
        if (Utils.isListEmpty(recentSongs)) {
            showEmptyState();
        } else {
            Collections.sort(recentSongs, new Comparator<Song>() {
                @Override
                public int compare(Song song1, Song song2) {
                    return song2.getLastPlayedDate().compareTo(song1.getLastPlayedDate());
                }
            });
            mView.changeViewState(ViewState.DATA);
            mView.showSongsList(true, recentSongs);
        }
    }

    @Override
    public void fetchSongs(String searchQuery) {
        mView.changeViewState(ViewState.LOADING);
        mDataManager.fetchSongs(searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SongResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SongResponse songResponse) {
                        if (!songResponse.getResults().isEmpty()) {
                            mView.changeViewState(ViewState.DATA);
                            mView.showSongsList(false, songResponse.getResults());
                        } else {
                            mView.changeViewState(ViewState.ERROR);
                            mView.showError(ErrorState.NO_RESULTS);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.changeViewState(ViewState.ERROR);
                        if (e instanceof NoInternetException) {
                            mView.showError(ErrorState.NO_INTERNET);
                        } else {
                            mView.showError(ErrorState.SOMETHING_WENT_WRONG);
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void retry(String query) {
        fetchSongs(query);
    }

    private void showEmptyState() {
        mView.changeViewState(ViewState.ERROR);
        mView.showError(ErrorState.EMPTY_LIST);
    }
}
