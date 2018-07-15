package com.semerson.networkassessment.activities.user.awareness.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.semerson.networkassessment.R;
import com.semerson.networkassessment.activities.WelcomeActivity;
import com.semerson.networkassessment.storage.AppStorage;

import java.util.ArrayList;

public class QuizHome extends AppCompatActivity {

    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String EXTRA_DIFFICULTY = "extraDifficulty";
    public static final String EXTRA_CATEGORY = "selected_category";

    //public static final String SHARED_PREFS = "sharedPrefs";
    public static final String KEY_HIGHSCORE = "keyHighscore";

    private TextView textViewHighscore;
    private Spinner spinnerDifficulty;
    private Spinner spinnerCateogories;

    private int highscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);

        textViewHighscore = findViewById(R.id.text_view_highscore);
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);
        spinnerCateogories = findViewById(R.id.spinner_categories);

        String[] difficultyLevels = Question.getAllDifficultyLevels();
        String[] categories = Question.getAllCategories();

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, difficultyLevels);
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);

        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapterDifficulty);
        spinnerCateogories.setAdapter(adapterCategory);

        loadHighscore();

        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);

        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

        if (AppStorage.getValue(this, AppStorage.CUSTOM_AWARENESS_QUIZ_REQUIRED, false)) {
            String difficulty = spinnerDifficulty.getSelectedItem().toString();

            ArrayList<String> categoriesForQuiz = new ArrayList<>();
            categoriesForQuiz.add(AppStorage.getValue(this, AppStorage.QUIZ_CATEGORY_1, categories[0]));
            categoriesForQuiz.add(AppStorage.getValue(this, AppStorage.QUIZ_CATEGORY_2, categories[1]));
            categoriesForQuiz.add(AppStorage.getValue(this, AppStorage.QUIZ_CATEGORY_3, categories[2]));
            String category = spinnerCateogories.getSelectedItem().toString();

            Intent intent = new Intent(QuizHome.this, QuizActivity.class);
            intent.putExtra(EXTRA_DIFFICULTY, difficulty);
            intent.putStringArrayListExtra(EXTRA_CATEGORY, categoriesForQuiz);
            startActivityForResult(intent, REQUEST_CODE_QUIZ);
        }
    }

    private void startQuiz() {
        String difficulty = spinnerDifficulty.getSelectedItem().toString();
        String category = spinnerCateogories.getSelectedItem().toString();

        Intent intent = new Intent(QuizHome.this, QuizActivity.class);
        intent.putExtra(EXTRA_DIFFICULTY, difficulty);
        String[] categoriesForQuiz = new String[1];
        categoriesForQuiz[0] = category;


        intent.putExtra(EXTRA_CATEGORY, category);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) {
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);
                if (score > highscore) {
                    updateHighscore(score);
                }
            }
        }
    }

    //The Spinners will need a listener to update the highscore depending on what is selected
    private void loadHighscore() {

        SharedPreferences prefs = getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        highscore = prefs.getInt(getHighScoreKey(), 0);
        textViewHighscore.setText("Highscore: " + highscore);
    }

    public String getHighScoreKey(){
        String category = spinnerCateogories.getSelectedItem().toString();
        String level = spinnerCateogories.getSelectedItem().toString();
        return KEY_HIGHSCORE + category + level;
    }

    private void updateHighscore(int highscoreNew) {
        highscore = highscoreNew;
        textViewHighscore.setText("Highscore: " + highscore);

        SharedPreferences prefs = getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(getHighScoreKey(), highscore);
        editor.apply();
    }
}
