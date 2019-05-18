package com.example.exoplayer_11;

import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.media)
    PlayerView playerView;
    private Long position = 0L;
    private final String CURRENT = "current";

    private SimpleExoPlayer simpleExoPlayer;
    private final String URL = "https://some.com/ssssOP.mp4";
    private final String URL_2 = "https://some.com/sss.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        position = savedInstanceState.getLong(CURRENT,0L);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (simpleExoPlayer!=null){

            position = simpleExoPlayer.getCurrentPosition();
            outState.putLong(CURRENT,position);
        }

    }


    private void initializePlayer(){

        simpleExoPlayer = ExoPlayerFactory.
                newSimpleInstance(new DefaultRenderersFactory(this),
                            new DefaultTrackSelector(),
                            new DefaultLoadControl());

        //bind the view with the player
        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(true);
        simpleExoPlayer.seekTo(position);

        //add the media souce
        Uri uri = Uri.parse(URL);
        MediaSource mediaSource = buildSouce(uri);
        simpleExoPlayer.prepare(mediaSource);
    }

    private MediaSource buildSouce(Uri uri){

        //for single resource

        return new
                 ExtractorMediaSource.
                         Factory
                 (new DefaultHttpDataSourceFactory("simpale"))
                  .createMediaSource(uri);


       //for multiple resources
       /*
       ExtractorMediaSource extractorMediaSource1

            = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("sample-1"))
               .createMediaSource(Uri.parse(URL));


        ExtractorMediaSource extractorMediaSource2
                = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory("sample-1"))
                .createMediaSource(Uri.parse(URL_2));

        return new ConcatenatingMediaSource(extractorMediaSource1,extractorMediaSource2);
        */


    }

    private void releasePlayer(){

        if (simpleExoPlayer!=null){

            position = simpleExoPlayer.getCurrentPosition();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(Util.SDK_INT>23)
            initializePlayer();
        Log.d("99", ""+Util.SDK_INT);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Util.SDK_INT<24  || simpleExoPlayer==null)
            initializePlayer();
        else
            simpleExoPlayer.seekTo(position);

    }


    @Override
    protected void onPause() {
        super.onPause();

        if (Util.SDK_INT<24)
            releasePlayer();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (Util.SDK_INT>23) {

            releasePlayer();

        }
        Log.d("99", "onStop: ");
    }

}
