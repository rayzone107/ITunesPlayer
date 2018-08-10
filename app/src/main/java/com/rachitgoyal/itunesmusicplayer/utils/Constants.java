package com.rachitgoyal.itunesmusicplayer.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Rachit Goyal on 13/06/18
 */

public class Constants {

    public static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss:SS a", Locale.getDefault());

    public interface MEDIA_TYPE {
        String MUSIC = "music";
    }

    public interface EXTRAS {
        String SONG = "SONG";
        String SONGS = "SONGS";
        String PAGE = "PAGE";
        String SONG_ID = "SONG_ID";
    }


}
