package com.semerson.networkassessment.activities.user.awareness.quiz.questions;

import com.semerson.networkassessment.activities.user.awareness.quiz.Question;

import java.util.ArrayList;
import java.util.List;

public class RansomwareQuestions {

    public static List<Question> getQuestions() {
        List<Question> questions = new ArrayList<>();
        Question q1 = new Question("This type of malware locks and encrypts your files until a ransom is paid:",
                "Rootkits",
                "Bots",
                "Hijack malware",
                "Ransomware",
                4, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q1);
        Question q2 = new Question("A Ransomware attack _____________ to all files.",
                "Blocks access",
                "Opens access",
                "Provides encryption",
                "Creates subfolders",
                1, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q2);
        Question q3 = new Question("Ransomware is typically found in these file types:",
                "Excel files, Jpeg, and screen saver files",
                "PDF files",
                "Macro enabled Office documents, executable files, and JavaScript files",
                "Macro Enabled Office documents, screen saver files, and JavaScript files",
                3, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q3);
        Question q4 = new Question("Users have been infected by compromised __________________________.",
                "All listed",
                "Ad networks",
                "Malicious files hosted on peer to peer file sharing sites",
                "Phishing emails",
                1, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q4);
        Question q5 = new Question("",
                "",
                "",
                "",
                "",
                1, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q5);
        Question q6 = new Question("Most commonly, ransomware attackers request payment via:",
                "PayPal",
                "Bitcoin",
                "Western Union",
                "Gift cards",
                2, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q6);
        Question q7 = new Question("The best way to resolve a ransomware attack?",
                "Don't pay the ransom",
                "Pay the ransom",
                "Try to contact the people behind the ransomware to sort a weekly payment plan",
                "None of the above",
                1, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q7);
        Question q8 = new Question("Ransomware can affect external drives if the drive is hooked up to your computer at the time of the attack.",
                "None of the above",
                "False",
                "Only if you browse files within the external drive",
                "True",
                4, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q8);
        Question q9 = new Question("What four strategies can be employed to help prevent ransomware attacks?",
                "Safe web browsing, don’t enable macros in attached documents, back up files regularly, and keep software up to date",
                "Use caution around links, only open executable files from a saved email, store old files off site, download from only known sources",
                "Keep software up to date, have IT open any strange attachments that are emailed to you, power down and shut off your computer at the end of the day, only use the incognito tab feature when browsing the web.",
                "None of the above",
                1, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q9);
        Question q10 = new Question("If your computer is infected by ransomware you should:",
                "Disconnect any external drives and networks",
                "Power down your computer",
                "Report the incident immediately",
                "All listed",
                4, Question.DIFFICULTY_EASY, Question.RANSOMWARE);
        questions.add(q10);
        return questions;
    }
}
