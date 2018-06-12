package com.semerson.networkassessment.activities.user.awareness;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.AuthenticationQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.MalwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.RansomwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.SocialEngineeringQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.SoftwareConfigurationQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.WebQuestions;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityAwarenessQuizFragment extends Fragment {

    private ProgressBar progressAuthEasy;
    private ProgressBar progressAuthMiddle;
    private ProgressBar progressAuthHard;

    private ProgressBar progressMalwareEasy;
    private ProgressBar progressMalwareMiddle;
    private ProgressBar progressMalwareHard;

    private ProgressBar progressMobileEasy;
    private ProgressBar progressMobileMiddle;
    private ProgressBar progressMobileHard;

    private ProgressBar progressRansomwareEasy;
    private ProgressBar progressRansomwareMiddle;
    private ProgressBar progressRansomwareHard;

    private ProgressBar progressSocialEngineeringEasy;
    private ProgressBar progressSocialEngineeringMiddle;
    private ProgressBar progressSocialEngineeringHard;

    private ProgressBar progressSoftwareConfigEasy;
    private ProgressBar progressSoftwareConfigMiddle;
    private ProgressBar progressSoftwareConfigHard;

    private ProgressBar progressWebEasy;
    private ProgressBar progressWebMiddle;
    private ProgressBar progressWebHard;

    public SecurityAwarenessQuizFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security_awareness_quiz, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstancesState) {

        progressAuthEasy = view.findViewById(R.id.progressBarAuthenticationEasy);
        progressAuthMiddle = view.findViewById(R.id.progressBarAuthenticationMedium);
        progressAuthHard = view.findViewById(R.id.progressBarAuthenticationHard);


        Button btn = view.findViewById(R.id.btnTest);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int progress = progressAuthEasy.getProgress() + 2;
                progressAuthEasy.setProgress(progress);
            }
        });
    }

    private void setProgressBars() {
        setProgressBarProgress(progressAuthEasy, AuthenticationQuestions.getEasyQuestions().size(),
                progressAuthEasy, AuthenticationQuestions.getMediumQuestions().size(),
                progressAuthEasy, AuthenticationQuestions.getHardQuestions().size());

        setProgressBarProgress(progressMalwareEasy, MalwareQuestions.getEasyQuestions().size(),
                progressMalwareMiddle, MalwareQuestions.getMediumQuestions().size(),
                progressMalwareHard, MalwareQuestions.getHardQuestions().size());

        setProgressBarProgress(progressMobileEasy, SocialEngineeringQuestions.getEasyQuestions().size(),
                progressMobileMiddle, SocialEngineeringQuestions.getMediumQuestions().size(),
                progressMobileHard, SocialEngineeringQuestions.getHardQuestions().size());

        setProgressBarProgress(progressRansomwareEasy, RansomwareQuestions.getEasyQuestions().size(),
                progressRansomwareMiddle, RansomwareQuestions.getMediumQuestions().size(),
                progressRansomwareHard, RansomwareQuestions.getHardQuestions().size());

        setProgressBarProgress(progressSocialEngineeringEasy, SocialEngineeringQuestions.getEasyQuestions().size(),
                progressSocialEngineeringMiddle, SocialEngineeringQuestions.getMediumQuestions().size(),
                progressSocialEngineeringHard, SocialEngineeringQuestions.getHardQuestions().size());

        setProgressBarProgress(progressSoftwareConfigEasy, SoftwareConfigurationQuestions.getEasyQuestions().size(),
                progressSoftwareConfigMiddle, SoftwareConfigurationQuestions.getMediumQuestions().size(),
                progressSoftwareConfigHard, SoftwareConfigurationQuestions.getHardQuestions().size());

        setProgressBarProgress(progressWebEasy, WebQuestions.getEasyQuestions().size(),
                progressWebMiddle, WebQuestions.getMediumQuestions().size(),
                progressWebHard, WebQuestions.getHardQuestions().size());
    }

    private void setProgressBarProgress(ProgressBar progressEasy, int easyProgress, ProgressBar progressMiddle, int middleProgress, ProgressBar progressHard, int hardProgress) {
        progressEasy.setProgress(easyProgress);
        progressMiddle.setProgress(middleProgress);
        progressHard.setProgress(hardProgress);
    }

    public static SecurityAwarenessQuizFragment newInstance() {
        SecurityAwarenessQuizFragment fragment = new SecurityAwarenessQuizFragment();
        Bundle bundle = new Bundle();
        //bundle.putParcelable("scan-results", scanResults);
        fragment.setArguments(bundle);

        return fragment;
    }

}
