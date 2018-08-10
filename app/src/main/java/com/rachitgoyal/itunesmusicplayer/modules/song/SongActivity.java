package com.rachitgoyal.itunesmusicplayer.modules.song;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rachitgoyal.itunesmusicplayer.R;
import com.rachitgoyal.itunesmusicplayer.models.Song;
import com.rachitgoyal.itunesmusicplayer.modules.base.BaseActivity;
import com.rachitgoyal.itunesmusicplayer.utils.Constants;
import com.rachitgoyal.itunesmusicplayer.utils.ErrorState;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongActivity extends BaseActivity implements SongContract.View {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.title_tv)
    TextView mTitleTV;

    @BindView(R.id.artist_album_tv)
    TextView mArtistAlbumTV;

    @BindView(R.id.image_iv)
    ImageView mImageIV;

    @BindView(R.id.play_pause_iv)
    ImageView mPlayPauseIV;

    @BindView(R.id.current_time_tv)
    TextView mCurrentTimeTV;

    @BindView(R.id.pending_time_tv)
    TextView mPendingTimeTV;

    @BindView(R.id.seek_bar)
    SeekBar mSeekbar;

    @BindView(R.id.buffering_pb)
    ProgressBar mBufferingPB;

    @BindView(R.id.list_iv)
    ImageView mListIV;

    @BindView(R.id.favourite_iv)
    ImageView mFavouriteIV;

    private SongContract.Presenter mPresenter;

    private boolean mIsPlaying;
    private MediaPlayer mMediaPlayer;
    private boolean mInitialStage = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPresenter = new SongPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mSeekbar.setEnabled(false);
        mSeekbar.setMax(30000);
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaPlayer != null && fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mPresenter.playSong(getIntent().getStringExtra(Constants.EXTRAS.SONG_ID));
    }

    @OnClick(R.id.play_pause_iv)
    public void onPlayPauseClick(View view) {
        playPause();
    }

    @OnClick(R.id.list_iv)
    public void onListClick(View view) {
        super.onBackPressed();
    }

    @OnClick(R.id.favourite_iv)
    public void onFavouriteClick(View view) {
        mPresenter.toggleFavourite();
    }

    @Override
    public void changeViewState(int viewState) {

    }

    @Override
    public void showError(ErrorState errorState) {

    }

    @Override
    public void setSongDetails(Song song) {
        mTitleTV.setText(song.getTrackName());
        String artistAlbum = song.getArtistName() + " | " + song.getCollectionName();
        mArtistAlbumTV.setText(artistAlbum);
        mCurrentTimeTV.setText(R.string.time_zero);
        mPendingTimeTV.setText(R.string.time_30_secs);

        Glide.with(mImageIV).load(song.getArtworkUrl100()).into(mImageIV);

        mFavouriteIV.setImageResource(song.isFavourite() ? R.drawable.ic_favourite_enabled : R.drawable.ic_favourite_disabled);
    }

    @Override
    public void playSong(Song song) {
        if (mInitialStage) {
            new MusicPlayer().execute(song.getPreviewUrl());
        }
        mIsPlaying = true;
    }

    @Override
    public void showAsFavourite(boolean isFavourite) {
        mFavouriteIV.setImageResource(isFavourite ? R.drawable.ic_favourite_enabled : R.drawable.ic_favourite_disabled);
    }

    private void playPause() {
        if (!mIsPlaying) {
            mPlayPauseIV.setImageResource(R.drawable.ic_pause);
            if (!mInitialStage && !mMediaPlayer.isPlaying()) {
                mMediaPlayer.start();
            }
            mIsPlaying = true;
        } else {
            mPlayPauseIV.setImageResource(R.drawable.ic_play);
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
            }
            mIsPlaying = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    class MusicPlayer extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            Boolean prepared;

            try {
                mMediaPlayer.setDataSource(strings[0]);
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mInitialStage = true;
                        mIsPlaying = false;
                        mPlayPauseIV.setImageResource(R.drawable.ic_play);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mMediaPlayer.prepare();
                prepared = true;

            } catch (Exception e) {
                e.printStackTrace();
                prepared = false;
            }

            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mPlayPauseIV.setVisibility(View.VISIBLE);
            mBufferingPB.setVisibility(View.GONE);
            mMediaPlayer.start();
            mSeekbar.setEnabled(true);

            mHandler = new Handler();
            SongActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mMediaPlayer != null) {
                        int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                        mSeekbar.setProgress(mCurrentPosition);
                        double progressDouble = ((double) Math.round((double) mCurrentPosition / 1000)) / 100;
                        mCurrentTimeTV.setText(String.valueOf(String.format(Locale.getDefault(), "%.2f", progressDouble)));
                        mPendingTimeTV.setText("- ".concat(String.valueOf(String.format(Locale.getDefault(), "%.2f", 0.30 - progressDouble))));
                    }
                    mHandler.postDelayed(this, 100);
                }
            });
            mInitialStage = false;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPlayPauseIV.setVisibility(View.INVISIBLE);
            mBufferingPB.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return true;
    }
}
