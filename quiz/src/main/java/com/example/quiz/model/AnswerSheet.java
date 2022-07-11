package com.example.quiz.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class AnswerSheet {
    private List<List<Boolean>> answers;
    private int quizId;
    private String quizName;

    private List<Question> questions;
    private List<String> texts;
    private List<List<String>> options;

    private int size;

    public AnswerSheet(Result result) {
        this(result.getQuiz());
        String[] parts = result.getAnswers().split(Quiz.SEPARATOR);
        for(int i = 0; i<parts.length; i++) {
            String[] parts2 = parts[i].split(Question.SEPARATOR);
            for(int j = 0; j<parts2.length; j++) {
                answers.get(i).set(j, true);
            }
        }
    }

    public AnswerSheet(Quiz quiz) {
        quizId = quiz.getId();
        quizName = quiz.getQuizName();

        questions = new ArrayList<>(quiz.getQuestions());
        size = questions.size();
        answers = new ArrayList<>();
        options = new ArrayList<>();
        texts = new ArrayList<>();
        for (Question question : questions) {
            texts.add(question.getQuestionText());
            List<String> qOptions = new ArrayList<>();
            List<Boolean> qAnswers = new ArrayList<>();
            String[] parts = question.getOptions().split(Question.SEPARATOR);
            for (String part : parts) {
                qOptions.add(part);
                qAnswers.add(false);
            }
            options.add(qOptions);
            answers.add(qAnswers);
        }
    }
}
