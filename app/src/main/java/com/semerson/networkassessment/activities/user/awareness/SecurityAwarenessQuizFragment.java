package com.semerson.networkassessment.activities.user.awareness;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semerson.networkassessment.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityAwarenessQuizFragment extends Fragment {


    public SecurityAwarenessQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security_awareness_quiz, container, false);
    }

    public static SecurityAwarenessQuizFragment newInstance() {
        SecurityAwarenessQuizFragment fragment = new SecurityAwarenessQuizFragment();
        Bundle bundle = new Bundle();
        //bundle.putParcelable("scan-results", scanResults);
        fragment.setArguments(bundle);

        return fragment;
    }

}
