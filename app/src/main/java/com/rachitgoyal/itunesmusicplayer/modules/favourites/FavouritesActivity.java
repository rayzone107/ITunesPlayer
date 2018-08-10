package com.rachitgoyal.itunesmusicplayer.modules.favourites;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BaseActivity;
import com.rachitgoyal.itunesmusicplayer.modules.song.SongActivity;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;
import com.rachitgoyal.itunesmusicplayer.utils.ViewState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rachitgoyal.itunesmusicplayer.utils.ViewState.DATA;
import static com.rachitgoyal.itunesmusicplayer.utils.ViewState.ERROR;
import static com.rachitgoyal.itunesmusicplayer.utils.ViewState.LOADING;

public class FavouritesActivity extends BaseActivity implements FavouritesContract.View, FavouritesItemViewHolder.OnItemClickListener,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    @BindView(R.id.base)
    CoordinatorLayout mBase;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.favourites_rl)
    RecyclerView mFavouritesRL;

    @BindView(R.id.error_layout)
    View mErrorLayout;

    @BindView(R.id.error_lav)
    LottieAnimationView mErrorLav;

    @BindView(R.id.error_title_tv)
    TextView mErrorTitleTV;

    @BindView(R.id.error_message_tv)
    TextView mErrorMessageTV;

    @BindView(R.id.error_button)
    Button mErrorButton;

    private FavouritesContract.Presenter mPresenter;
    private FavouritesAdapter mAdapter;
    private List<Song> mSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSongs = new ArrayList<>();

        mAdapter = new FavouritesAdapter(mSongs, this);
        mFavouritesRL.setLayoutManager(new LinearLayoutManager(mContext));
        mFavouritesRL.setAdapter(mAdapter);
        mFavouritesRL.setItemAnimator(new DefaultItemAnimator());

        mPresenter = new FavouritesPresenter(this);
        mPresenter.fetchFavourites();

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mFavouritesRL);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(mFavouritesRL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showFavourites(List<Song> favourites) {
        mSongs.clear();
        mSongs.addAll(favourites);
        mAdapter.clear();
        mAdapter.addSongs(favourites);
    }

    @Override
    public void changeViewState(@ViewState.ItemTypeDef int viewState) {
        switch (viewState) {
            case LOADING:
                mFavouritesRL.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.GONE);
                break;
            case DATA:
                mFavouritesRL.setVisibility(View.VISIBLE);
                mErrorLayout.setVisibility(View.GONE);
                break;
            case ERROR:
                mFavouritesRL.setVisibility(View.GONE);
                mErrorLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void showError(ErrorState errorState) {
        mErrorLav.setAnimation(errorState.getErrorLottieResId());
        mErrorLav.playAnimation();
        mErrorTitleTV.setText(errorState.getErrorTitleResId());
        mErrorMessageTV.setText(errorState.getErrorMessageResId());
        mErrorButton.setVisibility(errorState.isShowRetry() ? View.VISIBLE : View.GONE);
        mErrorButton.setText(errorState.getRetryTextResId());
    }

    @Override
    public void onItemClick(Song song, int position, ImageView sharedImageView) {
        mPresenter.onItemClick(song, position);
        mAdapter.notifyDataSetChanged();
        Intent intent = new Intent(FavouritesActivity.this, SongActivity.class);
        intent.putExtra(Constants.EXTRAS.SONG_ID, song.getTrackId());
        startActivity(intent);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavouritesItemViewHolder) {
            String name = mSongs.get(viewHolder.getAdapterPosition()).getTrackName();

            final Song deletedSong = mSongs.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            mAdapter.removeItem(viewHolder.getAdapterPosition());

            if (mAdapter.getItemCount() == 0) {
                changeViewState(ViewState.ERROR);
                showError(ErrorState.EMPTY_FAVOURTES_LIST);
            }

            Snackbar snackbar = Snackbar.make(mBase, name + " removed from favourites!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeViewState(ViewState.DATA);
                    mAdapter.restoreItem(deletedSong, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
