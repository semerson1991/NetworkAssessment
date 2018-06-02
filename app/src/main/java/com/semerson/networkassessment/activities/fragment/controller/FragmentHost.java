package com.semerson.networkassessment.activities.fragment.controller;

import android.app.Fragment;

public interface FragmentHost {
    Fragment getFragment(int fragmentID);
    void setFragment(Fragment fragment, boolean addToBackStack);

    void setYoutubeFragment(String url);
}
