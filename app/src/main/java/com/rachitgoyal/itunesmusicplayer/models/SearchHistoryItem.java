package com.rachitgoyal.itunesmusicplayer.models;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

public class SearchHistoryItem extends SugarRecord implements Serializable {

    public static final String QUERY_STRING = "QUERY_STRING";

    String queryString;
    Date timeOfSearch;

    public SearchHistoryItem() {
    }

    public SearchHistoryItem(String query, Date timeOfSearch) {
        this.queryString = query;
        this.timeOfSearch = timeOfSearch;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public Date getTimeOfSearch() {
        return timeOfSearch;
    }

    public void setTimeOfSearch(Date timeOfSearch) {
        this.timeOfSearch = timeOfSearch;
    }
}
