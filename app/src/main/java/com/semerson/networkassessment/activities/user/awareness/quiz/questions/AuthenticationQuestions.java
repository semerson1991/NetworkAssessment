package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationQuestions {

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(getEasyQuestions());
        questions.addAll(getMediumQuestions());
        questions.addAll(getHardQuestions());
        return questions;
    }

    public static List<Question> getEasyQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question("Which is true about passwords?",
                "Shorter passwords are more effective",
                "They are most effective when they are long, complex and diverse",
                "They are more secure without multi-factor authentication",
                "The most effective passwords contain only numbers and letters",
                2, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q1);
        Question q2 = new Question("This software is designed to make it easier to manage multiple, diverse passwords:",
                "Password safe (Password Manager software)",
                "Multi-factor authentication",
                "Web browser",
                "Malware",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q2);
        Question q3 = new Question("Which is true about the \"Remember Me?\" function of your Web browser?",
                "It is the safest option for storing passwords",
                "It is recommended for use on public computers",
                "It allows anyone with access to your browser to log into your account",
                "It is not a threat to security",
                3, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q3);
        Question q4 = new Question("Which is an example of multi-factor authentication?",
                "Single-use code sent to your cell phone",
                "QR Code",
                "These are all examples of multi-factor authentication",
                "Single-use code sent to your tablet",
                3, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q4);

        Question q6 = new Question("How can malware be used to steal your password?",
                "It allows anyone with access to your Web browser to log into your account",
                "Malware cannot be used to steal your password",
                "It attempts to crack your password with thousands of possible variations",
                "It allows attackers to monitor your activity remotely",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q6);

        Question q8 = new Question("Which is not true about data breaches?",
                "You can better protect yourself by changing your passwords every 90 days",
                "Organizations must disclose data breaches publicly",
                "A data breach can happen through no fault of your own",
                "Data breaches are typically made public immediately",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q8);
        Question q9 = new Question("Which of the following four passwords is the most secure?",
                "Boat123",
                "Password1",
                "123456789",
                "!Wtg5z$",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_AUTHENTICATION);
        questions.add(q9);
        return questions;
    }

    public static List<Question> getMediumQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q7 = new Question("How can you prevent malware from being used to steal your password?",
                "Be wary of links or attachments in emails and on the web",
                "You cannot prevent the use of malware to steal your password",
                "Use the \"Remember Me?\" function in your Web browser",
                "Use a long and complex password",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_AUTHENTICATION);
        questions.add(q7);
        return questions;
    }

    public static List<Question> getHardQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q5 = new Question("How can you prevent a brute force attack?",
                "Change your password every 90 days",
                "Use a long and complex password",
                "Keep your anti-virus up-to-date",
                "Be wary of links or attachments in emails",
                2, Question.DIFFICULTY_HARD, Question.CATEGORY_AUTHENTICATION);
        questions.add(q5);
        Question q6 = new Question("What is a brute force attack??",
                "The use of spyware",
                "Attempting to crack your password with thousands of possible variations",
                "Physically stealing the hard drive with your passwords on it",
                "The use of RAT malware",
                2, Question.DIFFICULTY_HARD, Question.CATEGORY_AUTHENTICATION);
        questions.add(q6);
        return questions;
    }
}
