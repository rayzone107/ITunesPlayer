package com.rachitgoyal.itunesmusicplayer.modules.favourites;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Song> mSongs;
    private FavouritesItemViewHolder.OnItemClickListener mItemClickListener;

    FavouritesAdapter(List<Song> songs, FavouritesItemViewHolder.OnItemClickListener onItemClickListener) {
        mSongs = songs;
        mItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);
        return new FavouritesItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((FavouritesItemViewHolder) holder).bindData(mSongs.get(position), position, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mSongs == null || mSongs.size() <= 0 ? 0 : mSongs.size();
    }

    void addSongs(List<Song> songs) {
        int size = getItemCount();
        if (mSongs == null) {
            mSongs = new ArrayList<>();
        }
        mSongs.addAll(songs);
        notifyItemRangeInserted(size, mSongs.size());
    }

    void clear() {
        if (mSongs != null && mSongs.size() > 0) {
            mSongs.clear();
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        Song removedSong = mSongs.get(position);
        removedSong.setFavourite(false);
        removedSong.save();
        mSongs.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Song song, int position) {
        mSongs.add(position, song);
        song.setFavourite(true);
        song.save();
        notifyItemInserted(position);
    }
}
