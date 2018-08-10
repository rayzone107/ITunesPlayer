package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.content.Context;

import com.rachitgoyal.itunesmusicplayer.models.SongResponse;
import com.rachitgoyal.itunesmusicplayer.network.ServiceAbstract;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;
import com.rachitgoyal.itunesmusicplayer.utils.NoInternetException;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import retrofit2.Call;
import retrofit2.Response;

public class ListServiceImpl extends ServiceAbstract implements ListServiceApi {

    private static final int MAX_FETCH_THRESHOLD = 24;

    ListServiceImpl(Context context) {
        super(context);
    }

    @Override
    public Observable<SongResponse> fetchSongs(final String query) {
        return Observable.create(new ObservableOnSubscribe<SongResponse>() {
            @Override
            public void subscribe(ObservableEmitter<SongResponse> emitter) throws Exception {
                if (isNetworkConnected()) {
                    Call<SongResponse> call = mNetworkService.getSongs(query, Constants.MEDIA_TYPE.MUSIC, MAX_FETCH_THRESHOLD);
                    try {
                        Response<SongResponse> response = call.execute();
                        if (response.isSuccessful()) {
                            SongResponse songResponse = response.body();
                            emitter.onNext(songResponse);
                        } else {
                            emitter.onError(new Error(response.message()));
                        }
                    } catch (IOException e) {
                        emitter.onError(e);
                    }
                } else {
                    emitter.onError(new NoInternetException());
                }
                emitter.onComplete();
            }
        });
    }
}
