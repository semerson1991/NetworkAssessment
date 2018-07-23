package com.semerson.networkassessment.controller;

import android.app.Fragment;

public interface FragmentHost {
    Fragment getFragment(int fragmentID);
    void setFragment(Fragment fragment, boolean addToBackStack);
    void setFragment(Fragment fragment, boolean addToBackStack, int fragmentIdToReplace);
}
