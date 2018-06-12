package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class SoftwareConfigurationQuestions {

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(getEasyQuestions());
        questions.addAll(getMediumQuestions());
        questions.addAll(getHardQuestions());
        return questions;
    }

    public static List<Question> getEasyQuestions() {
        List<Question> questions = new ArrayList<>();

        return questions;
    }

    public static List<Question> getMediumQuestions() {
        List<Question> questions = new ArrayList<>();

        return questions;
    }

    public static List<Question> getHardQuestions() {
        List<Question> questions = new ArrayList<>();

        return questions;
    }
}
