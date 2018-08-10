package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.SearchHistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchHistoryItem> mSearchHistoryItems;
    private SearchViewHolder.OnItemClickListener mItemClickListener;

    SearchAdapter(List<SearchHistoryItem> searchHistoryItems, SearchViewHolder.OnItemClickListener itemClickListener) {
        mSearchHistoryItems = searchHistoryItems;
        mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SearchViewHolder) holder).bindData(mSearchHistoryItems.get(position), position, mItemClickListener);
    }

    @Override
    public int getItemCount() {
        return mSearchHistoryItems.size();
    }

    void addQueries(List<SearchHistoryItem> searchHistoryItems) {
        int size = getItemCount();
        if (mSearchHistoryItems == null) {
            mSearchHistoryItems = new ArrayList<>();
        }
        mSearchHistoryItems.addAll(searchHistoryItems);
        notifyItemRangeInserted(size, mSearchHistoryItems.size());
    }

    void clear() {
        if (mSearchHistoryItems != null && mSearchHistoryItems.size() > 0) {
            mSearchHistoryItems.clear();
            notifyDataSetChanged();
        }
    }
}
