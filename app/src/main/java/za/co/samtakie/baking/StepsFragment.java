package za.co.samtakie.baking;


import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.activity.RecipeActivity;
import za.co.samtakie.baking.data.BakingContract;


/**
 * A simple {@link Fragment} subclass.
 * Last updated on the 2018/02/07, clean code
 * create an instance of this fragment.
 */
@SuppressWarnings("ALL")
public class StepsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleExoPlayer mExoplayer;

    LoaderManager.LoaderCallbacks<Cursor> mYourLoaderCallbacks = this;

    /* Unique ID for the loader */
    private static final int ID_ITEM_STEP_LOADER = 12;

    private String recipeName;
    private int position;
    private int countSteps;
    private String sPosition;
    private int positionSteps;
    private Uri itemStepUri;
    private long playerPosition;
    private boolean isThumbNail, isVideoUrl;
    private String videoUrl;
    private String thumbNail;

    /* Uri variable to hold the video or thumbnail data */
    private Uri builtUri;
    private Uri builtThumbnailUri;

    @BindView(R.id.playerView) SimpleExoPlayerView mPlayerView;
    @BindView(R.id.step_tv) TextView stepTextView;
    @BindView(R.id.nextBtn) Button nextBtn;
    @BindView(R.id.prevBtn) Button prevBtn;
    @BindView(R.id.noVideo_iv) ImageView noVideoImgV;


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getLoaderManager().initLoader(ID_ITEM_STEP_LOADER, null, this);

        /* Check for the first step and deactivate the Prev button,
        * if it is the last step deactivate the Next button */
        if(countSteps == (positionSteps+1)){
            nextBtn.setEnabled(false);
        } else {
            nextBtn.setEnabled(true);
        }

        if((positionSteps+1) == 1){
            prevBtn.setEnabled(false);
        } else {
            prevBtn.setEnabled(true);
        }

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_steps, container, false);

        /*Bind all the views with this Fragment*/
        ButterKnife.bind(this, view);

        if(savedInstanceState != null) {
            playerPosition = savedInstanceState.getLong("playerPosition");
            recipeName = savedInstanceState.getString("recipeName");
            videoUrl = savedInstanceState.getString("videoUrl");
            isThumbNail = savedInstanceState.getBoolean("isThumbNail");
            isVideoUrl = savedInstanceState.getBoolean("isVideoUrl");
            countSteps = savedInstanceState.getInt("countSteps");
            if(savedInstanceState.getString("builtUri") != null) {
                builtUri = Uri.parse(savedInstanceState.getString("builtUri"));
            }

            if(savedInstanceState.getString("builtThumbnailUri") != null) {
                builtThumbnailUri = Uri.parse(savedInstanceState.getString("builtThumbnailUri"));
            }

            if (isVideoUrl) {

                initializePlayer(builtUri, playerPosition, isThumbNail);
            } else if(isThumbNail){
                /* load the images if there is any or the default image from the placeHolder*/
                loadImage(builtThumbnailUri);
            } else {
                // hide the exoPlayer view
                mPlayerView.setVisibility(View.INVISIBLE);
            }


        }

        /*By default hide the imageview for the image incase there is no video to play*/
        noVideoImgV.setVisibility(View.INVISIBLE);

        /* Initiate the next button click*/
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPosition = ++positionSteps;
                int newDataStep;
                ++position;
                if(countSteps -1 == currentPosition){
                    nextBtn.setEnabled(false);
                }
                if(currentPosition > 0){
                    prevBtn.setEnabled(true);
                }

                /* If the current position is greater than 1 and equal and less than count step
                * assign the new data to newDataStep and passed it to the buildStepsItemUri.
                * This will load the next step.*/
                if(currentPosition >= 1 && currentPosition <= countSteps){
                    newDataStep = position;
                    itemStepUri = BakingContract.BakingEntry.buildStepsItemUri(newDataStep);
                }


                if(countSteps == currentPosition){
                    nextBtn.setEnabled(false);
                }
                getLoaderManager().restartLoader(ID_ITEM_STEP_LOADER, null, mYourLoaderCallbacks);
            }
        });

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* If the prevoius position is greater than or equal to zero substract one from
                * positionsSteps and position and passed the new position to the buildStepsItemUri
                * This will load the prevoius data */
                if(positionSteps >= 0){
                    --positionSteps;
                    --position;
                    itemStepUri = BakingContract.BakingEntry.buildStepsItemUri(position);
                }
                if(positionSteps == 0){
                    prevBtn.setEnabled(false);
                }

                if(countSteps > positionSteps){
                    nextBtn.setEnabled(true);
                }
                getLoaderManager().restartLoader(ID_ITEM_STEP_LOADER, null, mYourLoaderCallbacks);
            }
        });

        /* Don't change the view if the android_me_linear_layout is null, means we are in Tablet mode */
        if(getActivity().findViewById(R.id.android_me_linear_layout) == null){
            if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE) {

                nextBtn.setVisibility(View.INVISIBLE);
                prevBtn.setVisibility(View.INVISIBLE);
                stepTextView.setVisibility(View.INVISIBLE);

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPlayerView.getLayoutParams();
                params.width = params.MATCH_PARENT;
                params.height = params.MATCH_PARENT;
                mPlayerView.setLayoutParams(params);
            }

        }


        return view;
    }

    /***
     * This method will load an image if there is an image file, if there is a video file it will
     * load the default picture in the placeholder.
     * @param uri the link of the thumbnail
     * @param isThumbNail a boolean to check if there is no link
     */
    private void loadImage( Uri uri) {
            if(uri != null) {
                // Hide the exoPlayer view
                mPlayerView.setVisibility(View.INVISIBLE);
                String thumbNail = builtThumbnailUri.toString().substring(builtThumbnailUri.toString().lastIndexOf('.') + 1);
                //initializePlayer(builtThumbnailUri, playerPosition, isThumbNail);
                if (thumbNail.equals("mp4")) {

                    thumbNail = null;

                    // don't load the image load the default placeholder
                    Picasso.with(noVideoImgV.getContext())
                            .load(thumbNail)
                            .placeholder(R.drawable.no_video)
                            .into(noVideoImgV);
                } else {
                    // in this case the url is not a video file and this can be loaded in the imageView
                    Picasso.with(noVideoImgV.getContext())
                            .load(thumbNail)
                            .placeholder(R.drawable.no_video)
                            .into(noVideoImgV);
                }
            }else{
                String thumbNail = null;
                Picasso.with(noVideoImgV.getContext())
                        .load(thumbNail)
                        .placeholder(R.drawable.no_video)
                        .into(noVideoImgV);
            }

    }

    /**
     * Initialize ExoPlayer
     * @param mediaUri The Uri of the recipe steps video
     */
    private void initializePlayer(Uri mediaUri, long position, boolean isThumbnail){
        if(mExoplayer == null){
            // Create an instance of the Exoplayer
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if(position != C.TIME_UNSET && !isThumbnail){
                mExoplayer.seekTo(position);
            }
            mExoplayer.prepare(mediaSource);
            mExoplayer.setPlayWhenReady(true);

        }
    }

    /**
     * Release Exoplayer.
     */
    private void releasePlayer(){
        mExoplayer.stop();
        mExoplayer.release();
        mExoplayer = null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        /* Release the player if it has been initialized when the activity is destroyed. */
        if(mExoplayer != null) {
            releasePlayer();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        /* Resume the player if it has been initialized when the activity is destroyed. */
        if(mExoplayer == null) {

            if(builtUri != null){
                mPlayerView.setVisibility(View.VISIBLE);
                initializePlayer(builtUri, playerPosition, isThumbNail);
            } else if(builtThumbnailUri != null){
                mPlayerView.setVisibility(View.INVISIBLE);
                loadImage(builtThumbnailUri);
            } else {
                mPlayerView.setVisibility(View.INVISIBLE);
                Uri uri = null;
                loadImage(uri);
            }

        }
        playerPosition = 0;

    }

    @Override
    public void onPause() {
        super.onPause();
        /* Release the player if it has been initialized when the activity is destroyed. */
        if(mExoplayer != null) {
            playerPosition = mExoplayer.getCurrentPosition();
            releasePlayer();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch(loaderId){
            case ID_ITEM_STEP_LOADER:
                Uri recipeQueryUri = itemStepUri;
                return new CursorLoader(getActivity(),
                        recipeQueryUri,
                        RecipeActivity.STEPS_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()){
            cursorHasValidData = true;
        }

        if(!cursorHasValidData){

            return;
        }


        if(data.moveToFirst()){
            do {
                /* Get the description and videoUri data */
                String description = data.getString(data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEP_DESCRIPTION));
                String videoUri = data.getString(data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEP_VIDEOURL));
                String thumbnailURL = data.getString(data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEP_THUMBNAILURL));
                videoUrl = videoUri;
                stepTextView.setText(description);

                /* Uri variable set to null */
                builtUri = null;
                builtThumbnailUri = null;

                /* Check if there is a video link available or a thumbnail,
                if not use local image resource linked to the recipe name*/
                if (!videoUri.equals("") && !videoUri.equals(" ")) {
                    noVideoImgV.setVisibility(View.INVISIBLE);
                    builtUri = Uri.parse(videoUri).buildUpon().build();
                    isThumbNail = false;
                    isVideoUrl = true;
                } else if (!thumbnailURL.equals("") && !thumbnailURL.equals(" ")){
                    noVideoImgV.setVisibility(View.INVISIBLE);
                    builtThumbnailUri = Uri.parse(thumbnailURL).buildUpon().build();
                    isThumbNail = true;
                    isVideoUrl = false;
                    noVideoImgV.setVisibility(View.VISIBLE);
                } else {
                    noVideoImgV.setVisibility(View.VISIBLE);
                }
                if(mExoplayer != null) {

                    releasePlayer();
                    if(builtUri != null){
                        mPlayerView.setVisibility(View.VISIBLE);
                        initializePlayer(builtUri, playerPosition, isThumbNail);
                    } else if(builtThumbnailUri != null){
                        mPlayerView.setVisibility(View.INVISIBLE);
                        loadImage(builtThumbnailUri);
                    } else {
                        mPlayerView.setVisibility(View.INVISIBLE);
                        Uri uri = null;
                        loadImage(uri);
                    }


                } else {
                    if(builtUri != null){
                        mPlayerView.setVisibility(View.VISIBLE);
                        initializePlayer(builtUri, playerPosition, isThumbNail);
                    } else if(builtThumbnailUri != null){
                        mPlayerView.setVisibility(View.INVISIBLE);
                        loadImage(builtThumbnailUri);
                    } else {
                        mPlayerView.setVisibility(View.INVISIBLE);
                        Uri uri = null;
                        loadImage(uri);
                    }
                }

            } while(data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }


    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setCountSteps(int countSteps) {
        this.countSteps = countSteps;
    }

    public void setsPosition(String sPosition) {
        this.sPosition = sPosition;
    }

    public void setPositionSteps(int positionSteps) {
        this.positionSteps = positionSteps;
    }

    public void setItemStepUri(Uri itemStepUri) {
        this.itemStepUri = itemStepUri;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(!isThumbNail && isVideoUrl) {
            outState.putLong(getResources().getString(R.string.playerPosition), playerPosition);
        }
        outState.putString("recipeName", recipeName);
        outState.putString("videoUrl", videoUrl);
        outState.putBoolean("isThumbNail", isThumbNail);
        outState.putBoolean("isVideoUrl", isVideoUrl);
        outState.putInt("countSteps", countSteps);
        if(builtUri != null) {
            outState.putString("builtUri", builtUri.toString());
        }

        if(builtThumbnailUri != null) {
            outState.putString("builtThumbnailUri", builtThumbnailUri.toString());
        }
    }
}