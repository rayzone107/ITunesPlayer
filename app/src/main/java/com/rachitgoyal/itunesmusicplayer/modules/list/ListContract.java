package com.rachitgoyal.itunesmusicplayer.modules.list;

import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.ArrayList;

public interface ListContract {
    interface View {

        void showSongsList(boolean isRecent, ArrayList<Song> songs);

        void changeViewState(@ViewState.ItemTypeDef int viewState);

        void showError(ErrorState errorState);
    }

    interface Presenter {

        void fetchSongs(String searchQuery);

        void retry(String query);

        void fetchRecentSongs();
    }
}
