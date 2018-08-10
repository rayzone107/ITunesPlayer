package com.rachitgoyal.itunesmusicplayer.modules.list;

import com.rachitgoyal.itunesmusicplayer.models.SongResponse;

import io.reactivex.Observable;

public interface ListServiceApi {

    Observable<SongResponse> fetchSongs(String query);
}
