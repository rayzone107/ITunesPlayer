package com.rachitgoyal.itunesmusicplayer.models;

import com.orm.SugarRecord;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class Song extends SugarRecord implements Serializable {

    public static final String TRACK_ID = "TRACK_ID";
    public static final String IS_FAVOURITE = "IS_FAVOURITE";
    public static final String IS_PLAYING = "IS_PLAYING";

    String wrapperType;
    String kind;
    String artistId;
    String trackId;
    String artistName;
    String collectionName;
    String trackName;
    String previewUrl;
    String artworkUrl100;
    long trackTimeMillis;
    String primaryGenreName;
    boolean isStreamable;
    boolean isFavourite;
    String lastPlayed;
    boolean isPlaying;
    Date lastPlayedDate;

    public Song() {
    }

    public Song(String wrapperType, String kind, String artistId, String trackId, String artistName,
                String collectionName, String trackName, String previewUrl, String artworkUrl100,
                long trackTimeMillis, String primaryGenreName, boolean isStreamable,
                boolean isFavourite, String lastPlayed, boolean isPlaying) {
        this.wrapperType = wrapperType;
        this.kind = kind;
        this.artistId = artistId;
        this.trackId = trackId;
        this.artistName = artistName;
        this.collectionName = collectionName;
        this.trackName = trackName;
        this.previewUrl = previewUrl;
        this.artworkUrl100 = artworkUrl100;
        this.trackTimeMillis = trackTimeMillis;
        this.primaryGenreName = primaryGenreName;
        this.isStreamable = isStreamable;
        this.isFavourite = isFavourite;
        this.lastPlayed = lastPlayed;
        this.isPlaying = isPlaying;
        try {
            this.lastPlayedDate = Constants.sdf.parse(lastPlayed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getWrapperType() {
        return wrapperType;
    }

    public void setWrapperType(String wrapperType) {
        this.wrapperType = wrapperType;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getArtworkUrl100() {
        return artworkUrl100;
    }

    public void setArtworkUrl100(String artworkUrl100) {
        this.artworkUrl100 = artworkUrl100;
    }

    public long getTrackTimeMillis() {
        return trackTimeMillis;
    }

    public void setTrackTimeMillis(long trackTimeMillis) {
        this.trackTimeMillis = trackTimeMillis;
    }

    public String getPrimaryGenreName() {
        return primaryGenreName;
    }

    public void setPrimaryGenreName(String primaryGenreName) {
        this.primaryGenreName = primaryGenreName;
    }

    public boolean isStreamable() {
        return isStreamable;
    }

    public void setStreamable(boolean streamable) {
        isStreamable = streamable;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(String lastPlayed) {
        this.lastPlayed = lastPlayed;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Date getLastPlayedDate() {
        try {
            return Constants.sdf.parse(lastPlayed);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public void setLastPlayedDate(Date lastPlayedDate) {
        this.lastPlayedDate = lastPlayedDate;
    }
}
