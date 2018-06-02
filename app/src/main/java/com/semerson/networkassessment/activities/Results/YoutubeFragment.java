package com.semerson.networkassessment.activities.Results;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.fragment.controller.FragmentHost;

/**
 * A simple {@link Fragment} subclass.
 */
public class YoutubeFragment extends Fragment {

    private Context context;
    private Button btnPlay;
    private YouTubePlayerSupportFragment mYoutubePlayerFragment;
    private YouTubePlayer.OnInitializedListener onInitializedListener;
    private FragmentHost fragmentHost;
    private View rootview;
    public YoutubeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      rootview =  getActivity().getWindow().getDecorView().getRootView();
        View fragmentYoutubeView = inflater.inflate(R.layout.fragment_youtube, container, false);








      //  mYoutubeVideoTitle = (TextView)fragmentYoutubeView.findViewById(R.id.fragment_youtube_title);
      //  mYoutubeVideoDescription = (TextView)fragmentYoutubeView.findViewById(R.id.fragment_youtube_description);

        //mYoutubeVideoTitle.setText("a4NT5iBFuZs&t=239s");
      //  mYoutubeVideoDescription.setText(getArguments().getString(Resources.KEY_VIDEO_DESC));

        //VideoFragment.setTextToShare(getArguments().getString(Resources.KEY_VIDEO_URL));

        return fragmentYoutubeView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        YouTubePlayerFragment youtubeFragment = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtubeplayer_fragment);

        youtubeFragment.initialize("AIzaSyACYXSWMJ0Zedh_oNLSohyG5D8a2QV-dCw",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        //youTubePlayer.loadVideo("5xVh-7ywKpE");
                        youTubePlayer.cueVideo("5xVh-7ywKpE");
                        //    youTubePlayer.play();
                        Log.i("YoutubeFrag", "Loading Video");
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.i("YoutubeFrag", "FAILED");
                    }
                });
    }

    public void doo(){
        mYoutubePlayerFragment = new YouTubePlayerSupportFragment();
        mYoutubePlayerFragment.initialize("AIzaSyACYXSWMJ0Zedh_oNLSohyG5D8a2QV-dCw", onInitializedListener);

    }

    public static Fragment newInstance() {
        YoutubeFragment fragment = new YoutubeFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;

        try {
            fragmentHost = (FragmentHost) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement FragmentHost");
        }
    }
}
