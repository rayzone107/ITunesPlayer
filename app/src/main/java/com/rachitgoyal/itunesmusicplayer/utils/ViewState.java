package com.rachitgoyal.itunesmusicplayer.utils;

import android.support.annotation.IntDef;

/**
 * Created by Rachit Goyal on 13/06/18
 */

public class ViewState {
    public static final int LOADING = 0;
    public static final int ERROR = 1;
    public static final int DATA = 2;

    @IntDef({
            LOADING,
            ERROR,
            DATA
    })
    public @interface ItemTypeDef {
    }
}
