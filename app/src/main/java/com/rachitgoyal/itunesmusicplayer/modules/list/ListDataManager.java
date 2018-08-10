package com.rachitgoyal.itunesmusicplayer.modules.list;

import com.rachitgoyal.itunesmusicplayer.models.SongResponse;

import io.reactivex.Observable;

public class ListDataManager {

    private ListServiceApi mServiceApi;

    ListDataManager(ListServiceApi serviceApi) {
        mServiceApi = serviceApi;
    }

    Observable<SongResponse> fetchSongs(String query) {
        return mServiceApi.fetchSongs(query);
    }
}
