package com.example.quiz.service;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.Result;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import com.example.quiz.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {
    private QuizRepository quizRepository;
    private QuestionRepository questionRepository;
    private ResultRepository resultRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository, QuestionRepository questionRepository, ResultRepository resultRepository) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.resultRepository = resultRepository;

        if (quizRepository.findAll().isEmpty()) {
            Question q1 =
                    Question.builder().questionText("Сколько будет 2+2?").singleAnswer(true).options("5#10#4#8").correctAnswers("2").build();

            Question q2 =
                    Question.builder().questionText("Какие методы доступны в java в классе String?").singleAnswer(false).options("append()#length()#replace()").correctAnswers("1" +
                            "1#2#3").build();

            Quiz quiz = Quiz.builder().quizName("Test quiz").build();
            saveQuiz(quiz);

            q1.setQuiz(quiz);
            questionRepository.save(q1);
            q2.setQuiz(quiz);
            questionRepository.save(q2);
        }
    }

    public void saveQuiz(Quiz quiz) {
        quizRepository.save(quiz);
    }

    public Quiz findQuizById(int id) {
        return quizRepository.findQuizById(id);
    }

    public List<Quiz> findAllQuizes() {
        return quizRepository.findAll();
    }

    public void deleteQuiz(int id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            throw new IllegalStateException();
        }
        quizRepository.delete(quiz);
    }

    public List<Quiz> findAvailableQuizes(User user) {
        List<Quiz> allQuizes = quizRepository.findAll();
        allQuizes.removeAll(resultRepository.findNotAvailable(user));
        return allQuizes;
    }

    public void saveQuestion(Question question) {
        questionRepository.save(question);
    }
}
