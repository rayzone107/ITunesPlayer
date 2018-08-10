package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rachit Goyal on 13/06/18
 */

class SongListItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.song_cv)
    CardView mSongItemCV;

    @BindView(R.id.image_iv)
    ImageView mImageIV;

    @BindView(R.id.title_tv)
    TextView mTitleTV;

    @BindView(R.id.artist_album_tv)
    TextView mArtistAlbumTV;

    @BindView(R.id.favourite_iv)
    ImageView mFavouriteIV;

    SongListItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final Song song, final int position, final OnItemClickListener itemClickListener) {
        Song songInDb = Select.from(Song.class).where(Condition.prop(Song.TRACK_ID).eq(song.getTrackId())).first();
        if (songInDb == null) {
            songInDb = song;
        }

        Context context = mSongItemCV.getContext();
        mSongItemCV.setCardBackgroundColor(context.getResources().getColor(songInDb.isPlaying() ? R.color.selected_blue : R.color.white));

        final Song finalSongInDb = songInDb;
        mSongItemCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(finalSongInDb, position, mImageIV);
            }
        });

        ViewCompat.setTransitionName(mImageIV, songInDb.getTrackName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(mImageIV.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(songInDb.getArtworkUrl100())
                .into(mImageIV);

        mTitleTV.setText(songInDb.getTrackName());
        String artistAlbum = Utils.isStringEmpty(songInDb.getArtistName()) ? "" : songInDb.getArtistName();
        artistAlbum = artistAlbum.concat(!Utils.isStringEmpty(artistAlbum) && !Utils.isStringEmpty(songInDb.getCollectionName()) ? " | " : "");
        artistAlbum = artistAlbum.concat(Utils.isStringEmpty(songInDb.getCollectionName()) ? "" : songInDb.getCollectionName());
        mArtistAlbumTV.setText(artistAlbum);
        mFavouriteIV.setVisibility(songInDb.isFavourite() ? View.VISIBLE : View.GONE);
    }

    public interface OnItemClickListener {
        void onItemClick(Song song, int position, ImageView sharedImageView);
    }
}
