package com.basha.karim.baking.UI;

import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basha.karim.baking.R;
import com.basha.karim.baking.models.Step;
import com.basha.karim.baking.utils.ToastUtils;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by karim on 4/7/2018.
 */

public class RecipeStepDetailFragment extends Fragment {

    private static String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();
    public static String KEY_STEP_LIST_DETAIL_BUNDLE = "key-step_list_detail-bundle";
    public static String KEY_STEP_INDEX_DETAIL_BUNDLE = "key-step_index_detail-bundle";
    public static String KEY_VIDEO_POSITION_BUNDLE = "key-video_position-bundle";

    private ArrayList<Step> mStepList;
    private int mStepListIndex;
    private Step mStep;
    private long mVideoPosition;
    private SimpleExoPlayerView mExoPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private String mVideoURL;
    private Dialog mFullScreenDialog;
    private View mRootView;
    private boolean isFullscreen;
    private boolean isTablet;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (!isTablet)
            setHasOptionsMenu(true);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().getSupportFragmentManager().popBackStack("recipe", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        isFullscreen = getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mRootView = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);
        TextView stepDescriptionTextView = (TextView) mRootView.findViewById(R.id.tv_step_description);
        mExoPlayerView = (SimpleExoPlayerView) mRootView.findViewById(R.id.exoplayer_view);

        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mExoPlayerView.getLayoutParams();

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mExoPlayerView.setLayoutParams(params);
                ((LinearLayout) mRootView.findViewById(R.id.ll_step_detail_fragment)).addView(mExoPlayerView, 1);
                isFullscreen = false;
                mFullScreenDialog.dismiss();
                super.onBackPressed();
            }
        };

        if (savedInstanceState != null) {
            mStepListIndex = savedInstanceState.getInt(KEY_STEP_INDEX_DETAIL_BUNDLE);
            mStepList = savedInstanceState.getParcelableArrayList(KEY_STEP_LIST_DETAIL_BUNDLE);
            mVideoPosition = savedInstanceState.getLong(KEY_VIDEO_POSITION_BUNDLE);
        } else {
            mStepListIndex = getArguments().getInt(RecipesActivity.KEY_STEP_INDEX_DETAIL_EXTRA);
            mStepList = getArguments().getParcelableArrayList(RecipesActivity.KEY_STEP_LIST_DETAIL_EXTRA);
        }
        if (mStepList != null) {
            mStep = mStepList.get(mStepListIndex);
            stepDescriptionTextView.setText(mStep.getDescription());
            String thumbnailURL = mStep.getThumbnailURLString();
            if (thumbnailURL != null && !thumbnailURL.equals("")) {
                final Uri thumbnailUri = Uri.parse(thumbnailURL).buildUpon().build();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap thumbnail;
                            thumbnail = Picasso.with(getActivity()).load(thumbnailUri).get();
                            mExoPlayerView.setDefaultArtwork(thumbnail);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            mVideoURL = mStep.getVideoURLString();
            Log.d(LOG_TAG, "video url =" + mVideoURL);
            if (mVideoURL == null || mVideoURL.isEmpty()) {
                mExoPlayerView.setVisibility(View.GONE);
                TextView noVideoAvailableTextView = (TextView) mRootView.findViewById(R.id.tv_no_video_available);
                noVideoAvailableTextView.setVisibility(View.VISIBLE);
                noVideoAvailableTextView.setGravity(Gravity.CENTER);
            }
            if (!isTablet) {
                Button previousStepButton = (Button) mRootView.findViewById(R.id.btn_previous_step);
                Button nextStepButton = (Button) mRootView.findViewById(R.id.btn_next_step);
                previousStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mStepListIndex > 0) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(RecipesActivity.KEY_STEP_LIST_DETAIL_EXTRA, mStepList);
                            bundle.putInt(RecipesActivity.KEY_STEP_INDEX_DETAIL_EXTRA, mStepListIndex - 1);
                            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                            recipeStepDetailFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, recipeStepDetailFragment, RecipeDetailActivity.TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            ToastUtils.createToast(getActivity(), "You're already on the first step.", Toast.LENGTH_SHORT);
                        }
                    }
                });

                nextStepButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mStepListIndex < mStepList.size() - 1) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList(RecipesActivity.KEY_STEP_LIST_DETAIL_EXTRA, mStepList);
                            bundle.putInt(RecipesActivity.KEY_STEP_INDEX_DETAIL_EXTRA, mStepListIndex + 1);
                            RecipeStepDetailFragment recipeStepDetailFragment = new RecipeStepDetailFragment();
                            recipeStepDetailFragment.setArguments(bundle);
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, recipeStepDetailFragment, RecipeDetailActivity.TAG_RECIPE_DETAIL_STEP_FRAGMENT)
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            ToastUtils.createToast(getActivity(), "You're already on the last step.", Toast.LENGTH_SHORT);
                        }
                    }
                });
            }
        }
        return mRootView;
    }


    public void initializePlayer() {
        if (mExoPlayer == null && mVideoURL != null && !mVideoURL.isEmpty()) {
            Uri videoUri = Uri.parse(mVideoURL).buildUpon().build();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(new DefaultBandwidthMeter());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mExoPlayerView.setPlayer(mExoPlayer);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "BakingApp"), new DefaultBandwidthMeter());
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource videoSource = new ExtractorMediaSource(videoUri,
                    dataSourceFactory, extractorsFactory, null, null);
            if (isFullscreen && !isTablet) {
                ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
                mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                mFullScreenDialog.show();
            }
            mExoPlayer.seekTo(mVideoPosition);
            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
            mExoPlayerView.hideController();
        }
    }

    @Override
    public void onResume() {
        initializePlayer();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mStepList != null) {
            outState.putParcelableArrayList(KEY_STEP_LIST_DETAIL_BUNDLE, mStepList);
            outState.putInt(KEY_STEP_INDEX_DETAIL_BUNDLE, mStepListIndex);
        }
        if(mExoPlayer!=null)
            outState.putLong(KEY_VIDEO_POSITION_BUNDLE,mExoPlayer.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}

