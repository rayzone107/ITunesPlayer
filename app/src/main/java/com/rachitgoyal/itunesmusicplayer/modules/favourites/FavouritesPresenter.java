package com.rachitgoyal.itunesmusicplayer.modules.favourites;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BasePresenter;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.Utils;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.List;

public class FavouritesPresenter extends BasePresenter implements FavouritesContract.Presenter {

    private FavouritesContract.View mView;

    public FavouritesPresenter(FavouritesContract.View view) {
        mView = view;
    }

    @Override
    public void fetchFavourites() {
        List<Song> favourites = Select.from(Song.class).where(Condition.prop(Song.IS_FAVOURITE).eq(1)).list();
        if (Utils.isListEmpty(favourites)) {
            mView.changeViewState(ViewState.ERROR);
            mView.showError(ErrorState.EMPTY_FAVOURTES_LIST);
        } else {
            mView.changeViewState(ViewState.DATA);
            mView.showFavourites(favourites);
        }
    }

    @Override
    public void onItemClick(Song song, int position) {
        Song playingSong = Select.from(Song.class).where(Condition.prop(Song.IS_PLAYING).eq(1)).first();
        if (playingSong != null) {
            playingSong.setPlaying(false);
            playingSong.save();
        }

        song.setPlaying(true);
        song.save();
    }

}
