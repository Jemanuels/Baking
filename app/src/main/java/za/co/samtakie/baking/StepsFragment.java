package za.co.samtakie.baking;



import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import za.co.samtakie.baking.data.BakingContract;

/**
 * A simple {@link Fragment} subclass.
 * Last updated on the 2018/02/07, clean code
 * create an instance of this fragment.
 */
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

        // retain this fragment
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        getActivity().setTitle(recipeName);
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

        if(savedInstanceState != null){
            mExoplayer.seekTo(savedInstanceState.getLong("playerPosition"));
            Log.d("Player position ", savedInstanceState.getLong("playerPosition") + "");
            playerPosition = savedInstanceState.getLong("playerPosition");

        }


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_steps, container, false);

        ButterKnife.bind(this, view);

        noVideoImgV.setVisibility(View.INVISIBLE);

        final Context context = getContext();


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
                * assign the new data to newDataStep and past it to the buildStepsItemUri.
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



    /**
     * Initialize ExoPlayer
     * @param mediaUri The Uri of the recipe steps video
     */
    private void initializePlayer(Uri mediaUri, long position){
        if(mExoplayer == null){
            // Create an instance of the Exoplayer
            Handler mainHandler = new Handler();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            LoadControl loadControl = new DefaultLoadControl();
            mExoplayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoplayer);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getActivity(), "Baking");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            if(position != C.TIME_UNSET){
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
        playerPosition = 0;
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
            //Log.d("Data ", "There is no data in the cursor");
            return;
        }


        if(data.moveToFirst()){
            do {
                /* Get the description and videoUri data */
                String description = data.getString(data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEP_DESCRIPTION));
                String videoUri = data.getString(data.getColumnIndex(BakingContract.BakingEntry.COLUMN_STEP_VIDEOURL));

                stepTextView.setText(description);
                Uri builtUri = null;
                if (!videoUri.equals("") && !videoUri.equals(" ")) {
                    noVideoImgV.setVisibility(View.INVISIBLE);
                    builtUri = Uri.parse(videoUri).buildUpon().build();
                } else {
                    noVideoImgV.setVisibility(View.VISIBLE);
                }
                if(mExoplayer != null) {

                    releasePlayer();
                    if(builtUri != null){
                        mPlayerView.setEnabled(true);
                        initializePlayer(builtUri, playerPosition);
                        /*Log.d("Player ", builtUri.toString());*/


                    } else {
                        mPlayerView.setVisibility(View.INVISIBLE);
                    }


                } else {
                    if(builtUri != null){
                        mPlayerView.setVisibility(View.VISIBLE);
                        initializePlayer(builtUri, playerPosition);
                        /*Log.d("Player ", builtUri.toString());*/


                    } else {
                        mPlayerView.setVisibility(View.INVISIBLE);
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
        outState.putLong(getResources().getString(R.string.playerPosition), mExoplayer.getCurrentPosition());
    }

}