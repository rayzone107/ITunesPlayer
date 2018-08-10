package com.rachitgoyal.itunesmusicplayer.network;

import com.rachitgoyal.itunesmusicplayer.models.SongResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rachit Goyal on 13/06/18
 */

public interface NetworkService {

    @GET("search")
    Call<SongResponse> getSongs(@Query("term") String query, @Query("media") String media, @Query("limit") int limit);

}
