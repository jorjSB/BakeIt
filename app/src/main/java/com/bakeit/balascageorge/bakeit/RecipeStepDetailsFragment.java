package com.bakeit.balascageorge.bakeit;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakeit.balascageorge.bakeit.models.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;


public class RecipeStepDetailsFragment extends Fragment {
    @Nullable
    @BindView(R.id.step_short_description)
    TextView stepShortDescription;

    @Nullable
    @BindView(R.id.player_view)
    PlayerView simpleExoPlayerView;

    @BindView(R.id.video_container)
    FrameLayout videoContainer;

    @BindView(R.id.no_video)
    ImageView noVideoImage;

    @BindView(R.id.imageView)
    ImageView mStepImage;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String STEP_TAG = "step";
    private Step mStep;
    private static final String TWO_PANE_TAG = "twoPane";
    private Boolean mTwoPane;
    private Unbinder unbinder;
    private SimpleExoPlayer player;
    private long playbackPosition = 0;
    private int currentWindow = 0;
    private boolean playWhenReady = true;
    private boolean mExoPlayerFullscreen = false;
    private Dialog mFullScreenDialog;
    private String KEY_AUTO_PLAY = "video_state";
    private String KEY_WINDOW = "video_window";
    private String KEY_POSITION = "video_position";

    public RecipeStepDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
//        setRetainInstance(true);

        if (getArguments() != null) {
            mStep = getArguments().getParcelable(STEP_TAG);
            mTwoPane = getArguments().getBoolean(TWO_PANE_TAG);
        }

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(KEY_AUTO_PLAY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }

        initFullscreenDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recipe_step_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(mStep != null)
            updateView(mStep);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void updateView(Step value) {
        mStep = value;

        // portrait
        if(mStep != null && simpleExoPlayerView != null)
            Objects.requireNonNull(stepShortDescription).setText(mStep.getDescription());

        if(mStep != null && !mStep.getThumbnailURL().isEmpty())
            Picasso.get().load(mStep.getThumbnailURL()).into(mStepImage);

        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle(mStep.getShortDescription());


        // handle player
        if(player != null)
            releasePlayer();

        if (Util.SDK_INT > 23)
            initializePlayer();
    }

    private void initializePlayer() {
        Log.d("exoplayer", "initPlayer");

        if(mStep == null)
            return;

        if(mStep.getVideoURL().isEmpty()){
            videoContainer.setVisibility(View.GONE);
            noVideoImage.setVisibility(View.VISIBLE);
            return;
        }else {
            videoContainer.setVisibility(View.VISIBLE);
            noVideoImage.setVisibility(View.GONE);
        }

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        // port
        if(simpleExoPlayerView != null)
            simpleExoPlayerView.setPlayer(player);

        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);

        Uri uri = Uri.parse(mStep.getVideoURL());
        MediaSource mediaSource = buildMediaSource(uri);

        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(currentWindow, playbackPosition);
        }
        player.prepare(mediaSource, !haveStartPosition, false);

        if(!mTwoPane)
            if(getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE )
                showFullscreenVideo();
            else
                hideFullscreenVideo();
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("BakeIt")).
                createMediaSource(uri);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        // hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();
    }


    private void hideFullscreenVideo() {
        Objects.requireNonNull(stepShortDescription).setVisibility(View.VISIBLE);

        ((ViewGroup) Objects.requireNonNull(simpleExoPlayerView).getParent()).removeView(simpleExoPlayerView);
        videoContainer.addView(simpleExoPlayerView);
        mExoPlayerFullscreen = false;
        if(mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }


    private void showFullscreenVideo() {
        Objects.requireNonNull(stepShortDescription).setVisibility(View.GONE);

        videoContainer.removeView(simpleExoPlayerView);
        mFullScreenDialog.addContentView(Objects.requireNonNull(simpleExoPlayerView), new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }

    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(Objects.requireNonNull(getContext()), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen)
                    hideFullscreenVideo();
                super.onBackPressed();
            }
        };
    }

    private void releasePlayer() {
        Log.d("exoplayer", "releasePlayer");
        if (player != null) {
            updateStartPosition();
            player.release();
            player = null;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(mTwoPane)
            return;

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            hideFullscreenVideo();
        }

        else if (newConfig.orientation == ORIENTATION_LANDSCAPE) {
            showFullscreenVideo();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        updateStartPosition();
        outState.putBoolean(KEY_AUTO_PLAY, playWhenReady);
        outState.putInt(KEY_WINDOW, currentWindow);
        outState.putLong(KEY_POSITION, playbackPosition);

    }

    private void updateStartPosition() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            currentWindow = player.getCurrentWindowIndex();
            playbackPosition = Math.max(0, player.getContentPosition());
        }
    }

}
