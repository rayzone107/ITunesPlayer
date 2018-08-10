package com.rachitgoyal.itunesmusicplayer.modules.list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.SearchHistoryItem;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.search_rl)
    RelativeLayout mSearchRL;

    @BindView(R.id.query_tv)
    TextView mQueryTV;

    SearchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindData(final SearchHistoryItem searchHistoryItem, final int position, final OnItemClickListener itemClickListener) {

        mQueryTV.setText(searchHistoryItem.getQueryString());
        mSearchRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onItemClick(searchHistoryItem, position);
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(SearchHistoryItem searchHistoryItem, int position);
    }
}
