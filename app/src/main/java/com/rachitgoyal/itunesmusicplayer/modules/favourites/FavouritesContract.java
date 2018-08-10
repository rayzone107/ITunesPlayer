package com.rachitgoyal.itunesmusicplayer.modules.favourites;

import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.List;

public interface FavouritesContract {
    interface View {

        void showFavourites(List<Song> favourites);

        void changeViewState(@ViewState.ItemTypeDef int viewState);

        void showError(ErrorState errorState);
    }

    interface Presenter {

        void fetchFavourites();

        void onItemClick(Song song, int position);
    }
}

