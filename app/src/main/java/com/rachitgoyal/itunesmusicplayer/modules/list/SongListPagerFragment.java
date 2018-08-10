package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BaseFragment;
import com.rachitgoyal.itunesmusicplayer.modules.search.SearchActivity;
import com.rachitgoyal.itunesmusicplayer.modules.song.SongActivity;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;
import com.rachitgoyal.itunesmusicplayer.utils.SpanningLinearLayoutManager;

import java.util.ArrayList;

public class SongListPagerFragment extends BaseFragment implements SongListItemViewHolder.OnItemClickListener {

    RecyclerView mSongsRV;
    private SongListAdapter mSongListAdapter;
    private ArrayList<Song> mSongs;
    public int mPage;

    public SongListPagerFragment() {
    }

    public static SongListPagerFragment newInstance(ArrayList<Song> songs, int page) {
        SongListPagerFragment fragment = new SongListPagerFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.EXTRAS.PAGE, page);
        args.putSerializable(Constants.EXTRAS.SONGS, songs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_song_list, container, false);
        mSongsRV = rootView.findViewById(R.id.song_rv);

        if (getArguments() != null) {
            mSongs = (ArrayList<Song>) getArguments().getSerializable(Constants.EXTRAS.SONGS);
            mPage = getArguments().getInt(Constants.EXTRAS.PAGE);
        }

        mSongListAdapter = new SongListAdapter(mSongs, this);

        mSongsRV.setLayoutManager(new SpanningLinearLayoutManager(mContext));
        mSongsRV.setAdapter(mSongListAdapter);

        return rootView;
    }

    @Override
    public void onItemClick(Song song, int position, ImageView sharedImageView) {
        Song playingSong = Select.from(Song.class).where(Condition.prop(Song.IS_PLAYING).eq(1)).first();
        if (playingSong != null) {
            playingSong.setPlaying(false);
            playingSong.save();
        }

        Song songInDb = Select.from(Song.class).where(Condition.prop(Song.TRACK_ID).eq(song.getTrackId())).first();
        if (songInDb == null) {
            songInDb = song;
        }
        songInDb.setPlaying(true);
        songInDb.save();

        mSongListAdapter.notifyDataSetChanged();
        Intent intent = new Intent(mContext, SongActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                sharedImageView, ViewCompat.getTransitionName(sharedImageView));
        intent.putExtra(Constants.EXTRAS.SONG_ID, songInDb.getTrackId());
        startActivity(intent, options.toBundle());
    }

    public void update(ArrayList<Song> songs) {
        mSongs.clear();
        mSongs.addAll(songs);
        mSongListAdapter.clear();
        mSongListAdapter.addSongs(songs);
        mSongListAdapter.notifyDataSetChanged();
    }
}


