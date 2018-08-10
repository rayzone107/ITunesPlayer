package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rachit Goyal on 13/06/18
 */

public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Song> mSongs;
    private SongListItemViewHolder.OnItemClickListener mItemClickListener;

    SongListAdapter(List<Song> songs, SongListItemViewHolder.OnItemClickListener onItemClickListener) {
        mSongs = songs;
        mItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SongListItemViewHolder) holder).bindData(mSongs.get(position), position, mItemClickListener);
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
}
