package com.semerson.networkassessment.activities.user.awareness;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.semerson.networkassessment.R;
import com.semerson.networkassessment.controller.FragmentHost;
import com.semerson.networkassessment.activities.user.awareness.quiz.Question;
import com.semerson.networkassessment.activities.user.awareness.quiz.QuizActivity;
import com.semerson.networkassessment.activities.user.awareness.quiz.QuizDbHelper;
import com.semerson.networkassessment.activities.user.awareness.quiz.QuizHighScore;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.AuthenticationQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.MalwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.MobileSecurityQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.RansomwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.SocialEngineeringQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.SoftwareConfigurationQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.WebQuestions;
import com.semerson.networkassessment.storage.AppStorage;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SecurityAwarenessQuizFragment extends Fragment {

    public static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY = "selected_category";
    public static final String EXTRA_CURRENT_HIGHSCORE = "current_high_score";

    private ProgressBar progressAuthEasy;
    private ProgressBar progressAuthMedium;
    private ProgressBar progressAuthHard;

    private ProgressBar progressMalwareEasy;
    private ProgressBar progressMalwareMedium;
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

    private Spinner spinnerDifficulty;
    private Spinner spinnerCateogories;

    private Context context;
    private FragmentHost fragmentHost;

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
        progressAuthMedium = view.findViewById(R.id.progressBarAuthenticationMedium);
        progressAuthHard = view.findViewById(R.id.progressBarAuthenticationHard);

        progressMalwareEasy = view.findViewById(R.id.progressBarMalwareEasy);
        progressMalwareMedium = view.findViewById(R.id.progressBarMalwareMedium);
        progressMalwareHard = view.findViewById(R.id.progressBarMalwareHard);

        progressMobileEasy = view.findViewById(R.id.progressBarMobileEasy);
        progressMobileMiddle = view.findViewById(R.id.progressBarMobileMedium);
        progressMobileHard = view.findViewById(R.id.progressBarMobileHard);

        progressRansomwareEasy = view.findViewById(R.id.progressBarRansomwareEasy);
        progressRansomwareMiddle = view.findViewById(R.id.progressBarRansomwareMedium);
        progressRansomwareHard = view.findViewById(R.id.progressBarRansomwareHard);

        progressSocialEngineeringEasy = view.findViewById(R.id.progressBarSocialEngineeringEasy);
        progressSocialEngineeringMiddle = view.findViewById(R.id.progressBarSocialEngineeringMedium);
        progressSocialEngineeringHard = view.findViewById(R.id.progressBarSocialEngineeringHard);

        progressSoftwareConfigEasy = view.findViewById(R.id.progressBarSoftwareSecEasy);
        progressSoftwareConfigMiddle = view.findViewById(R.id.progressBarSoftwareSecMedium);
        progressSoftwareConfigHard = view.findViewById(R.id.progressBarSoftwareSecHard);

        progressWebEasy = view.findViewById(R.id.progressBarWebSecSecEasy);
        progressWebMiddle = view.findViewById(R.id.progressBarWebSecSecMedium);
        progressWebHard = view.findViewById(R.id.progressBarWebSecSecHard);

        setProgressBars();

        spinnerDifficulty = view.findViewById(R.id.spinner_difficulty);
        spinnerCateogories = view.findViewById(R.id.spinner_categories);

        String[] difficultyLevels = Question.getAllDifficultyLevels();
        String[] categories = Question.getAllCategories();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, difficultyLevels);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, categories);

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
        spinnerCateogories.setAdapter(adapterCategory);

        Button buttonStartQuiz = view.findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

    }

    private void startQuiz() {
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String category = spinnerCateogories.getSelectedItem().toString();

        Intent intent = new Intent(context, QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        intent.putExtra(EXTRA_CATEGORY, category);
        intent.putExtra(EXTRA_CURRENT_HIGHSCORE, getPreviousQuizScore(category, difficulty));
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    private void setProgressBars() {
        setProgressBarMax(progressAuthEasy, AuthenticationQuestions.getEasyQuestions().size(),
                progressAuthMedium, AuthenticationQuestions.getMediumQuestions().size(),
                progressAuthHard, AuthenticationQuestions.getHardQuestions().size());

        setProgressBarMax(progressMalwareEasy, MalwareQuestions.getEasyQuestions().size(),
                progressMalwareMedium, MalwareQuestions.getMediumQuestions().size(),
                progressMalwareHard, MalwareQuestions.getHardQuestions().size());

        setProgressBarMax(progressMobileEasy, MobileSecurityQuestions.getEasyQuestions().size(),
                progressMobileMiddle, MobileSecurityQuestions.getMediumQuestions().size(),
                progressMobileHard, MobileSecurityQuestions.getHardQuestions().size());

        setProgressBarMax(progressRansomwareEasy, RansomwareQuestions.getEasyQuestions().size(),
                progressRansomwareMiddle, RansomwareQuestions.getMediumQuestions().size(),
                progressRansomwareHard, RansomwareQuestions.getHardQuestions().size());

        setProgressBarMax(progressSocialEngineeringEasy, SocialEngineeringQuestions.getEasyQuestions().size(),
                progressSocialEngineeringMiddle, SocialEngineeringQuestions.getMediumQuestions().size(),
                progressSocialEngineeringHard, SocialEngineeringQuestions.getHardQuestions().size());

        setProgressBarMax(progressSoftwareConfigEasy, SoftwareConfigurationQuestions.getEasyQuestions().size(),
                progressSoftwareConfigMiddle, SoftwareConfigurationQuestions.getMediumQuestions().size(),
                progressSoftwareConfigHard, SoftwareConfigurationQuestions.getHardQuestions().size());

        setProgressBarMax(progressWebEasy, WebQuestions.getEasyQuestions().size(),
                progressWebMiddle, WebQuestions.getMediumQuestions().size(),
                progressWebHard, WebQuestions.getHardQuestions().size());

        setProgressBarProgress();
    }

    private void setProgressBarMax(ProgressBar progressEasy, int easyProgress, ProgressBar progressMiddle, int middleProgress, ProgressBar progressHard, int hardProgress) {
        progressEasy.setMax(easyProgress);
        progressMiddle.setMax(middleProgress);
        progressHard.setMax(hardProgress);
    }

    private void setProgressBarProgress() {
        QuizDbHelper dbHelper = new QuizDbHelper(context);
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        List<QuizHighScore> quizHighScoreList = dbHelper.getCategoriesHighScores();

        for (QuizHighScore quizHighScore : quizHighScoreList) {
            switch (quizHighScore.getCategory()) {
                case Question.CATEGORY_AUTHENTICATION:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressAuthEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressAuthMedium.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressAuthHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_MALWARE:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressMalwareEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressMalwareMedium.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressMalwareHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_MOBILE_SEC:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressMobileEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressMobileMiddle.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressMobileHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_RANSOMWARE:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressRansomwareEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressRansomwareMiddle.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressRansomwareHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_SOCIAL_ENGINEERING:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressSocialEngineeringEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressSocialEngineeringMiddle.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressSocialEngineeringHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_SOFTWARE_SEC:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressSoftwareConfigEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressSoftwareConfigMiddle.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressSoftwareConfigHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
                case Question.CATEGORY_WEB_SEC:
                    switch (quizHighScore.getDifficulty()) {
                        case Question.DIFFICULTY_EASY:
                            progressWebEasy.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_MEDIUM:
                            progressWebMiddle.setProgress(quizHighScore.getHighscore());
                            break;
                        case Question.DIFFICULTY_HARD:
                            progressWebHard.setProgress(quizHighScore.getHighscore());
                            break;
                    }
                    break;
            }
        }
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = context.getSharedPreferences(AppStorage.APP_PREFERENCE, Context.MODE_PRIVATE);

        boolean resumedFromQuizFinished = preferences.getBoolean(QuizActivity.QUIZ_FINISHED, false);

        if (resumedFromQuizFinished) {
            int score = preferences.getInt(QuizActivity.EXTRA_SCORE, 0);

            QuizDbHelper dbHelper = new QuizDbHelper(context);
            dbHelper.onCreate(dbHelper.getWritableDatabase());

            String selectedCategory = spinnerCateogories.getSelectedItem().toString();
            String selectedDifficulty = spinnerDifficulty.getSelectedItem().toString();

            // String categoryDbColumnName = getCategoryColumnName(selectedCategory);
            // String difficultyDbColumnName = getDifficultyColumnName(selectedDifficulty);
            Integer currentHighScore = dbHelper.getCurrentHighScore(selectedCategory, selectedDifficulty);

            if (currentHighScore < score) {
                dbHelper.updateCategoryHighScore(selectedCategory, selectedDifficulty, score);
            }
            preferences.edit().remove(QuizActivity.QUIZ_FINISHED).commit();

            preferences.edit().remove(QuizActivity.EXTRA_SCORE).commit();
            preferences.edit().commit();
            setProgressBarProgress();
        }
    }

    private Integer getPreviousQuizScore(String category, String difficulty) {
        QuizDbHelper dbHelper = new QuizDbHelper(context);
        dbHelper.onCreate(dbHelper.getWritableDatabase());
        return dbHelper.getCurrentHighScore(category, difficulty);
    }

    public static SecurityAwarenessQuizFragment newInstance() {
        SecurityAwarenessQuizFragment fragment = new SecurityAwarenessQuizFragment();
        Bundle bundle = new Bundle();
        //bundle.putParcelable("scan-results", scanResults);
        fragment.setArguments(bundle);

        return fragment;
    }

}
