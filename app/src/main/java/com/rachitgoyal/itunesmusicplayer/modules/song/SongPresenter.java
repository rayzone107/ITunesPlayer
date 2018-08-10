package com.rachitgoyal.itunesmusicplayer.modules.song;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BasePresenter;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;

import java.util.Date;

public class SongPresenter extends BasePresenter implements SongContract.Presenter {

    private SongContract.View mView;

    private String mSongId;

    SongPresenter(SongContract.View view) {
        mView = view;
    }

    @Override
    public void playSong(String songId) {
        mSongId = songId;
        Song song = Select.from(Song.class).where(Condition.prop(Song.TRACK_ID).eq(songId)).first();

        String currentTime = Constants.sdf.format(new Date());
        song.setLastPlayed(currentTime);
        song.save();

        String mImageUrl = song.getArtworkUrl100();
        mImageUrl = mImageUrl.replaceAll("100x100", "500x500");
        song.setArtworkUrl100(mImageUrl);
        mView.setSongDetails(song);
        mView.playSong(song);
    }

    @Override
    public void toggleFavourite() {
        Song song = Select.from(Song.class).where(Condition.prop(Song.TRACK_ID).eq(mSongId)).first();
        boolean isFavourite = !song.isFavourite();
        song.setFavourite(isFavourite);
        song.save();
        mView.showAsFavourite(isFavourite);
    }
}
