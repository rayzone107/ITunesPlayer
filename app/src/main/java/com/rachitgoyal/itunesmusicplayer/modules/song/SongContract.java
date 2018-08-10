package com.rachitgoyal.itunesmusicplayer.modules.song;

import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

public interface SongContract {
    interface View {

        void changeViewState(@ViewState.ItemTypeDef int viewState);

        void showError(ErrorState errorState);

        void setSongDetails(Song song);

        void playSong(Song song);

        void showAsFavourite(boolean isFavourite);
    }

    interface Presenter {

        void playSong(String songId);

        void toggleFavourite();

    }
}
