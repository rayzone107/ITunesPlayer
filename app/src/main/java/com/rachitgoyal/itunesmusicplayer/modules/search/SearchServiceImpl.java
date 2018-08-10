package com.rachitgoyal.itunesmusicplayer.modules.search;

import android.content.Context;

import com.rachitgoyal.itunesmusicplayer.network.ServiceAbstract;

class SearchServiceImpl extends ServiceAbstract implements SearchServiceApi {

    private static final int MAX_FETCH_THRESHOLD = 24;

    SearchServiceImpl(Context context) {
        super(context);
    }

}
