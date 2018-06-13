package com.semerson.networkassessment.activities.user.awareness.quiz;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.semerson.networkassessment.activities.user.awareness.quiz.QuizContract.*;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.AuthenticationQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.MalwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.MobileSecurityQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.RansomwareQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.SocialEngineeringQuestions;
import com.semerson.networkassessment.activities.user.awareness.quiz.questions.WebQuestions;
import com.semerson.networkassessment.storage.AppStorage;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class QuizDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "securityAwarenessQuiz.db";
    public static final int DATABASE_VERSION = 1;
    private Context context;
    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;

        SharedPreferences prefs = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE);
        boolean dbCreated = prefs.getBoolean(AppStorage.DATABASE_CREATED, false);
        if (!dbCreated) {
            final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    QuestionsTable.TABLE_NAME + " ( " +
                    QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                    QuestionsTable.COLUMN_OPTION4 + " TEXT, " +
                    QuestionsTable.COLUMN_ANSWER_NR + " INTEGER, " +
                    QuestionsTable.COLUMN_DIFFICULTY + " TEXT," +
                    QuestionsTable.COLUMN_CATEGORY + " TEXT" +
                    ")";

            final String SQL_CREATE_CATEGORY_SCORE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    CategoryScore.TABLE_NAME + " ( " +
                    CategoryScore.COLUMN_CATEGORY + " TEXT," +
                    CategoryScore.COLUMN_DIFFICULTY + " TEXT," +
                    CategoryScore.COLUMN_HIGHSCORE + " INTEGER" +
                    ")";

            db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
            db.execSQL(SQL_CREATE_CATEGORY_SCORE_TABLE);
            fillQuestionsTable();
            fillCategoryScoreTable();

            SharedPreferences.Editor editor = context.getSharedPreferences(AppStorage.APP_PREFERENCE, MODE_PRIVATE).edit();
            editor.putBoolean(AppStorage.DATABASE_CREATED, true);
            editor.apply();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() {
        List<Question> allQuestions = new ArrayList<>();
        allQuestions.addAll(AuthenticationQuestions.getAllQuestions());
        allQuestions.addAll(MalwareQuestions.getAllQuestions());
        allQuestions.addAll(MobileSecurityQuestions.getAllQuestions());
        allQuestions.addAll(RansomwareQuestions.getAllQuestions());
        allQuestions.addAll(SocialEngineeringQuestions.getAllQuestions());
        //  allQuestions.addAll(SoftwareConfigurationQuestions.getAllQuestions());
        allQuestions.addAll(WebQuestions.getAllQuestions());

        for (Question question : allQuestions) {
            addQuestion(question);
        }
    }

    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionsTable.COLUMN_DIFFICULTY, question.getDifficulty());
        cv.put(QuestionsTable.COLUMN_CATEGORY, question.getCategory());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    private void fillCategoryScoreTable() {
        createCategoryHighScore(Question.CATEGORY_AUTHENTICATION, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_AUTHENTICATION, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_AUTHENTICATION, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_MALWARE, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_MALWARE, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_MALWARE, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_MOBILE_SEC, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_MOBILE_SEC, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_MOBILE_SEC, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_RANSOMWARE, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_RANSOMWARE, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_RANSOMWARE, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_SOCIAL_ENGINEERING, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_SOCIAL_ENGINEERING, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_SOCIAL_ENGINEERING, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_SOFTWARE_SEC, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_SOFTWARE_SEC, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_SOFTWARE_SEC, Question.DIFFICULTY_HARD, 0);

        createCategoryHighScore(Question.CATEGORY_WEB_SEC, Question.DIFFICULTY_EASY, 0);
        createCategoryHighScore(Question.CATEGORY_WEB_SEC, Question.DIFFICULTY_MEDIUM, 0);
        createCategoryHighScore(Question.CATEGORY_WEB_SEC, Question.DIFFICULTY_HARD, 0);

    }

    public void createCategoryHighScore(String category, String difficulty, Integer score) {
        ContentValues cv = new ContentValues();
        cv.put(CategoryScore.COLUMN_CATEGORY, category);
        cv.put(CategoryScore.COLUMN_DIFFICULTY, difficulty);
        cv.put(CategoryScore.COLUMN_HIGHSCORE, score);
        db.insert(CategoryScore.TABLE_NAME, null, cv);
    }

    public void updateCategoryHighScore(String category, String difficulty, Integer newHighScore) {
        ContentValues cv = new ContentValues();
        cv.put(CategoryScore.COLUMN_HIGHSCORE, newHighScore);

        db.update(CategoryScore.TABLE_NAME,
                cv,
                CategoryScore.COLUMN_CATEGORY + " = ? AND " + CategoryScore.COLUMN_DIFFICULTY + " = ?",
                new String[]{category, difficulty});
        }

    public QuizHighScore getQuizHighScoreRow(String category, String difficulty){
        String[] selectedArgs = new String[]{difficulty, category};
        Cursor c = db.rawQuery("SELECT * FROM " + CategoryScore.TABLE_NAME +
                " WHERE " + CategoryScore.COLUMN_DIFFICULTY + " = ? AND " + CategoryScore.COLUMN_CATEGORY +
                " = + ?", selectedArgs);

        QuizHighScore quizHighScore = new QuizHighScore();
        if (c.moveToFirst()) {
            quizHighScore.setDifficulty(c.getString(c.getColumnIndex(CategoryScore.COLUMN_DIFFICULTY)));
            quizHighScore.setCategory(c.getString(c.getColumnIndex(CategoryScore.COLUMN_CATEGORY)));
            quizHighScore.setHighscore(c.getInt(c.getColumnIndex(CategoryScore.COLUMN_HIGHSCORE)));
            c.close();
        }
        return quizHighScore;
    }

    public List<QuizHighScore> getCategoriesHighScores() {
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + CategoryScore.TABLE_NAME, null);
        List<QuizHighScore> quizHighScores = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                String category = (c.getString(c.getColumnIndex(CategoryScore.COLUMN_CATEGORY)));
                String difficulty = (c.getString(c.getColumnIndex(CategoryScore.COLUMN_DIFFICULTY)));
                Integer score = (c.getInt(c.getColumnIndex(CategoryScore.COLUMN_HIGHSCORE)));
                QuizHighScore quizHighScore = new QuizHighScore(category, difficulty, score);
                quizHighScores.add(quizHighScore);
            } while (c.moveToNext());
        }
        c.close();
        return quizHighScores;
    }

    public Integer getCurrentHighScore(String category, String difficulty) {

        String[] selectedArgs = new String[]{difficulty, category};
        Cursor c = db.rawQuery("SELECT * FROM " + CategoryScore.TABLE_NAME +
                        " WHERE " + CategoryScore.COLUMN_DIFFICULTY + " = + ? AND " + CategoryScore.COLUMN_CATEGORY +
                " = + ?", selectedArgs);

        Integer highScore = 0;
        if (c.moveToFirst()) {

            highScore = (c.getInt(c.getColumnIndex(CategoryScore.COLUMN_HIGHSCORE)));
            c.close();
        }
        return highScore;
    }

    public ArrayList<Question> getAllQuestions() {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }

    public ArrayList<Question> getQuestions(String difficulty, String category) {
        ArrayList<Question> questionList = new ArrayList<>();
        db = getReadableDatabase();

        String[] selectionArgs = new String[]{difficulty};
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME +
                " WHERE " + QuestionsTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                question.setDifficulty(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_DIFFICULTY)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_CATEGORY)));
                if (question.getCategory().equals(category)) {
                    questionList.add(question);
                }
            } while (c.moveToNext());
        }
        c.close();
        return questionList;
    }
}