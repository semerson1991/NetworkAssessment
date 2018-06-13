package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class SocialEngineeringQuestions {


    public static List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        questions.addAll(getEasyQuestions());
        questions.addAll(getMediumQuestions());
        questions.addAll(getHardQuestions());
        return questions;
    }

    public static List<Question> getEasyQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question("What weakness does social engineering exploit?",
                "Technical",
                "Neither human nor technical",
                "Both human and technical",
                "Human",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q1);
        Question q2 = new Question("Social engineering attacks can occur:",
                "All of these choices",
                "At home",
                "At work",
                "On any device",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q2);
        Question q3 = new Question("Social engineering attacks are best identified by:",
                "Spam filters",
                "Anti-virus software",
                "Critical thinking",
                "Firewalls",
                3, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q3);
        Question q4 = new Question("Which is not a type of social engineering attack?",
                "Zero-day exploit",
                "Phishing",
                "Tailgating",
                "Spam",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q4);

        Question q6 = new Question("Most spam emails are sent:",
                "For monetary gain",
                "For sensitive information",
                "To annoy the recipient",
                "To only a few people at a time",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q6);

        Question q9 = new Question("Which of the following should you never do?",
                "Any of these actions",
                "Click on a link from an unknown sender",
                "Use a drive you see laying around",
                "Respond to a request for sensitive information",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q9);
        Question q10 = new Question("If you spot suspicious activity, you should:",
                "Play along",
                "Respond to it",
                "Ignore it",
                "Report it",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q10);
        Question q11 = new Question("Which is not a characteristic of a general phishing message?",
                "Sent to thousands at a time",
                "Opportunistic",
                "Highly personalized",
                "Easy to spot",
                3, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q11);
        Question q12 = new Question("Which is a characteristic of a spear phishing message?",
                "All of the choices are characteristics of spear phishing",
                "Sent to small groups",
                "Targeted",
                "Highly Personalized",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q12);
        Question q13 = new Question("Why are spam filters less likely to stop a spear phishing attack?\n",
                "Emails are sent in small batches",
                "Emails are sent in large batches",
                "Emails never contain malware",
                "Spam filters catch most spear phishing emails",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q13);
        Question q14 = new Question("How many employees must fall for a phish to compromise our organization?",
                "1",
                "10",
                "100",
                "1000",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q14);

        Question q17 = new Question("Spear phishers may seek the following information in a data entry attack?",
                "Password",
                "Credit card number",
                "Data entry attacks do not solicit information",
                "Both password & credit card number",
                4, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q17);
        Question q18 = new Question("Which of the following is a warning sign of a spear phishing attack?",
                "All of these choices",
                "Comes from an unknown sender",
                "Contains spelling or grammatical errors",
                "Appeals to your emotions",
                1, Question.DIFFICULTY_EASY, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q18);
        return questions;
    }

    public static List<Question> getMediumQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q5 = new Question("Which action is an example of pretexting/bohoing?",
                "Impersonating someone from the IT department",
                "Leaving a flash drive in a coffee shop",
                "Following closely behind someone into a locked building",
                "Responding to an imaginary question",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q5);
        Question q7 = new Question("Which is an example of baiting?",
                "Neither of the provided answers",
                "Uploading a malicious file to a popular website",
                "Leaving a flash drive where someone will find it",
                "Both of the provided answers",
                4, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q7);
        Question q8 = new Question("Which action is an example of tailgating?",
                "Following closely behind someone into a locked building",
                "Responding to an imaginary question",
                "Sending an email to thousands at a time",
                "Impersonating someone from the IT department",
                1, Question.DIFFICULTY_MEDIUM, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q8);
        return questions;
    }

    public static List<Question> getHardQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q15 = new Question("This type of spear phishing attack solicits information through a login form:",
                "Attachment Attack",
                "Drive By Attack",
                "Click Only Attack",
                "Data Entry Attack",
                4, Question.DIFFICULTY_HARD, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q15);
        Question q16 = new Question("This type of spear phishing attack uses links to deliver malware to your computer:",
                "Click Only Attack",
                "Attachment Attack",
                "Data Entry Attack",
                "None of these choices",
                1, Question.DIFFICULTY_HARD, Question.CATEGORY_SOCIAL_ENGINEERING);
        questions.add(q16);
        return questions;
    }
}
