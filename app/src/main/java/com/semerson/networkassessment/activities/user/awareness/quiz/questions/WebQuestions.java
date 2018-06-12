package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class WebQuestions {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question("What does SSL stand for?",
                "Secure Sockets Layer",
                "Standard Security Layer",
                "Safe & Secure Link",
                "Solid State Link",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q1);
        Question q2 = new Question("What does an SSL connection do?",
                "Loads malware onto your computer",
                "Scans websites for malware",
                "Creates a firewall",
                "Sends data through a secure \"tunnel\"",
                3, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q2);
        Question q3 = new Question("The URL of a website containing SSL should contain:",
                "HTTPS",
                "HTTP",
                "FTP",
                "Mailto",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q3);
        Question q4 = new Question("Why can you not automatically trust a website with SSL encryption?",
                "SSL certificates can be forged",
                "Any website with SSL is secure",
                "A site that handles your data should not have SSL",
                "Data that is sent through SSL cannot be recovered",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q4);
        Question q5 = new Question("Which is not correct about software updates?",
                "They are a popular lure in phishing scams",
                "They can remove security flaws",
                "They can improve the user experience",
                "They are safe to download from an email attachment",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q5);
        Question q6 = new Question("What should you do if you observe malicious activity on the Web?",
                "Report it",
                "Ignore it",
                "Respond to it",
                "Any of these answers are appropriate",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q6);
        Question q7 = new Question("Which of the following is an example of a “phishing” attack?",
                "Sending someone an email that contains a malicious link that is disguised to look like an email from someone the person knows",
                "Creating a fake website that looks nearly identical to a real website in order to trick users into entering their login information",
                "Sending someone a text message that contains a malicious link that is disguised to look like a notification that the person has won a contest",
                "All of the above",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q7);
        Question q8 = new Question("What does the “https://” at the beginning of a URL denote, as opposed to \"http://\" (without the “s”)?",
                "That information entered into the site is encrypted",
                "That the site is the newest version available",
                "That the site is not accessible to certain computers",
                "That the information entered into the site is not encrypted",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_WEB_SEC);
        questions.add(q8);
        Question q9 = new Question("How can plug-ins or extensions make you vulnerable?",
                "Bloatware",
                "Malware",
                "Unpatched security flaws",
                "All of these answers",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_WEB_SEC);
        questions.add(q9);
        return questions;
    }
}
