package com.rachitgoyal.itunesmusicplayer.models;

import java.util.ArrayList;

public class SongResponse {

    int resultCount;
    ArrayList<Song> results;

    public SongResponse() {
    }

    public SongResponse(int resultCount, ArrayList<Song> results) {
        this.resultCount = resultCount;
        this.results = results;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public ArrayList<Song> getResults() {
        return results;
    }

    public void setResults(ArrayList<Song> results) {
        this.results = results;
    }
}
