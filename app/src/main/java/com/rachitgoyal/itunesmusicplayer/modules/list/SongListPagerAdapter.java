package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.rachitgoyal.itunesmusicplayer.models.Song;

import java.util.ArrayList;

public class SongListPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Song> mSongs;

    SongListPagerAdapter(FragmentManager fm, ArrayList<Song> songs) {
        super(fm);
        mSongs = songs;
    }

    @Override
    public Fragment getItem(int position) {
        return SongListPagerFragment.newInstance(getRelevantSongs(position), position);
    }

    @Override
    public int getCount() {
        return mSongs.size() / 6 + (mSongs.size() % 6 == 0 ? 0 : 1);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        SongListPagerFragment f = (SongListPagerFragment) object;
        f.update(getRelevantSongs(f.mPage));
        return super.getItemPosition(object);
    }

    public void updateSongs(ArrayList<Song> songs) {
        mSongs = songs;
    }

    private ArrayList<Song> getRelevantSongs(int page) {
        ArrayList<Song> songsAtPage = new ArrayList<>();
        int countOnPage = Math.min(mSongs.size(), (page + 1) * 6);
        for (int i = page * 6; i < countOnPage; i++) {
            songsAtPage.add(mSongs.get(i));
        }
        return songsAtPage;
    }

    public void clear() {
        mSongs.clear();
        notifyDataSetChanged();
    }
}
