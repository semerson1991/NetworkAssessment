package com.semerson.networkassessment.activities.user.awareness.quiz;

public class QuizHighScore {
    public String category;
    public String difficulty;
    public Integer highscore;

    public QuizHighScore(String category, String difficulty, Integer highscore){
        this.category = category;
        this.difficulty = difficulty;
        this.highscore = highscore;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public Integer getHighscore() {
        return highscore;
    }

    public void setHighscore(Integer highscore) {
        this.highscore = highscore;
    }
}
