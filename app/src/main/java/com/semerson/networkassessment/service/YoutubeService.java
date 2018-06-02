package com.semerson.networkassessment.service;

import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeService extends YouTubeBaseActivity {

    private Button btnPlay;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer.OnInitializedListener onInitializedListener;

    public YoutubeService(){

    }

    public void create(final YouTubePlayerView youTubePlayerView, Button buttonPlay) {
        this.youTubePlayerView = (YouTubePlayerView) youTubePlayerView;
        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("a4NT5iBFuZs&t=239s");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        btnPlay = (Button) buttonPlay;
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyACYXSWMJ0Zedh_oNLSohyG5D8a2QV-dCw", onInitializedListener);
            }
        });
    }
}
