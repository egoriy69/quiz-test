package com.example.quiz.controller;

import com.example.quiz.model.*;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.ResultService;
import com.example.quiz.service.UserService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ResultService resultService;

    @GetMapping(value = "/user/quiz/{id}")
    public ModelAndView answerQuiz(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        AnswerSheet answerSheet = new AnswerSheet(quizService.findQuizById(id));
        modelAndView.addObject("sheet", answerSheet);
        modelAndView.setViewName("user/quiz");
        return modelAndView;
    }

    @PostMapping(value = "/user/quiz/{id}")
    public ModelAndView answerQuiz(@PathVariable("id") int quizId, @ModelAttribute("sheet") AnswerSheet sheet,
                                   BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Result result = evaluateAnswerSheet(quizService.findQuizById(quizId), sheet, user);
        resultService.saveResult(result);
        modelAndView.addObject("quizes", quizService.findAvailableQuizes(user));
        modelAndView.setViewName("user/home");
        return modelAndView;
    }


    private static Result evaluateAnswerSheet(Quiz quiz, AnswerSheet answerSheet, User user) {
        int score = 0;
        List<String> answers = new ArrayList<>();
        for (int i = 0; i < answerSheet.getSize(); i++) {
            List<String> selected = new ArrayList<>();
            for (int j = 0; j < answerSheet.getAnswers().get(i).size(); j++) {
                Boolean b = answerSheet.getAnswers().get(i).get(j);
                if (b != null && b) {
                    selected.add(String.valueOf(j));
                }
            }
            String joined = String.join(Question.SEPARATOR, selected);
            if (!selected.isEmpty()) {
                if (quiz.getQuestions().get(i).getCorrectAnswers().equals(joined)) {
                    score++;
                } else {
                    score--;
                }
            }
            answers.add(joined);

        }
        return Result.builder().user(user).score(score).quiz(quiz).dateTime(new Date()).answers(String.join(Quiz.SEPARATOR, answers)).build();
    }
}
