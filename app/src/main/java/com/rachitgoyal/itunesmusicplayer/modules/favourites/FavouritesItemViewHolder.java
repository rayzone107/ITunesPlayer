package com.rachitgoyal.itunesmusicplayer.modules.favourites;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavouritesItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.view_foreground)
    CardView mViewForeground;

    @BindView(R.id.image_iv)
    ImageView mImageIV;

    @BindView(R.id.title_tv)
    TextView mTitleTV;

    @BindView(R.id.artist_album_tv)
    TextView mArtistAlbumTV;

    @BindView(R.id.favourite_iv)
    ImageView mFavouriteIV;

    FavouritesItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final Song song, final int position, final OnItemClickListener itemClickListener) {
        Context context = mViewForeground.getContext();
        mViewForeground.setCardBackgroundColor(context.getResources().getColor(song.isPlaying() ? R.color.selected_blue : R.color.white));

        mViewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(song, position, mImageIV);
            }
        });

        ViewCompat.setTransitionName(mImageIV, song.getTrackName());

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(mImageIV.getContext())
                .setDefaultRequestOptions(requestOptions)
                .load(song.getArtworkUrl100())
                .into(mImageIV);

        mTitleTV.setText(song.getTrackName());
        String artistAlbum = Utils.isStringEmpty(song.getArtistName()) ? "" : song.getArtistName();
        artistAlbum = artistAlbum.concat(!Utils.isStringEmpty(artistAlbum) && !Utils.isStringEmpty(song.getCollectionName()) ? " | " : "");
        artistAlbum = artistAlbum.concat(Utils.isStringEmpty(song.getCollectionName()) ? "" : song.getCollectionName());
        mArtistAlbumTV.setText(artistAlbum);
        mFavouriteIV.setVisibility(View.GONE);
    }

    public interface OnItemClickListener {
        void onItemClick(Song song, int position, ImageView sharedImageView);
    }
}
