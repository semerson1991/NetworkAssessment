package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class MobileSecurityQuestions {

    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(getEasyQuestions());
        questions.addAll(getMediumQuestions());
        questions.addAll(getHardQuestions());
        return questions;
    }

    public static List<Question> getEasyQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question("Which statement is not true?",
                "Application stores can be used to distribute malware",
                "Many phishing messages cater to mobile users",
                "Most online adults use a smartphone",
                "Mobile users are not a target of cyber criminals",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_MOBILE_SEC);
        questions.add(q1);

        Question q4 = new Question("A biometric lock uses ____ recognition technology to verify your identity.",
                "Fingerprint",
                "Password",
                "Pattern",
                "PIN",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_MOBILE_SEC);
        questions.add(q4);

        Question q8 = new Question("What is not covered by a BYOD policy?",
                "Which devices our organization provides to you",
                "Which devices you may bring",
                "What applications you may use on a device",
                "Maintenance and troubleshooting services",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_MOBILE_SEC);
        questions.add(q8);
        return questions;
    }

    public static List<Question> getMediumQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q6 = new Question("What does a remote wipe do?",
                "Erases all data from your phone",
                "Helps you to find your device",
                "Locks your phone with a message",
                "Transfers data from one device to another",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_MOBILE_SEC);
        questions.add(q6);
        Question q5 = new Question("What does a virtual private network do?",
                "Encrypts all transmissions from your device",
                "Blocks connections to wireless networks",
                "Adds password protection to a public network",
                "Makes it easier for attackers to intercept information",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_MOBILE_SEC);
        questions.add(q5);
        return questions;
    }

    public static List<Question> getHardQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q2 = new Question("What is a \"smish?\"",
                "A phish that is sent via text message",
                "Another name for spam",
                "Any attack that utilizes malware",
                "A mobile application loaded with malware",
                1, Question.DIFFICULTY_HARD, Question.CATEGORY_MOBILE_SEC);
        questions.add(q2);
        Question q3 = new Question("Which is not true about a smish?",
                "Uses phishing tactics",
                "Contains shortened links",
                "Sent to an email inbox",
                "Uses pictures, coupons, or games as a lure",
                3, Question.DIFFICULTY_HARD, Question.CATEGORY_MOBILE_SEC);
        questions.add(q3);
        Question q7 = new Question("Which of the following is a variety of password-protected network?",
                "Neither",
                "WPA",
                "WEP",
                "Both",
                4, Question.DIFFICULTY_HARD, Question.CATEGORY_MOBILE_SEC);
        questions.add(q7);
        return questions;
    }


}
