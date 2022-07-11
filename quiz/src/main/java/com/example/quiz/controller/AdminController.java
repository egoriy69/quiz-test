package com.example.quiz.controller;

import com.example.quiz.model.*;
import com.example.quiz.service.QuizService;
import com.example.quiz.service.ResultService;
import com.example.quiz.service.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private QuizService quizService;

    @Autowired
    private ResultService resultService;

    @GetMapping(value = "/admin/results")
    public ModelAndView results() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        List<Result> results = resultService.findAllResults();
        results.sort((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()));
        List<Result> filtered = new ArrayList<>();
        for (Result r : results) {
            if (filtered.stream().anyMatch(res -> res.getQuiz().getId() == r.getQuiz().getId() && res.getUser().getId() == r.getUser().getId())) {
                continue;
            }
            filtered.add(r);
        }
        modelAndView.addObject("results", filtered);
        modelAndView.setViewName("admin/results");
        return modelAndView;
    }

    @GetMapping(value = "/admin/results/{id}")
    public ModelAndView singleResult(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        Result result = resultService.findResultById(id);
        modelAndView.addObject("sheet", new AnswerSheet(result));
        modelAndView.setViewName("admin/singleResult");
        return modelAndView;
    }

    @GetMapping(value = "/admin/quizes")
    public ModelAndView quizes() {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");

        List<Quiz> quizes = quizService.findAllQuizes();
        modelAndView.addObject("quizes", quizes);
        modelAndView.setViewName("admin/quizes");
        return modelAndView;
    }

    @PostMapping(value = "/admin/quizes/delete/{id}")
    public ModelAndView deleteQuiz(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        quizService.deleteQuiz(id);

        List<Quiz> quizes = quizService.findAllQuizes();
        modelAndView.addObject("quizes", quizes);
        modelAndView.setViewName("admin/quizes");
        return modelAndView;
    }

    @GetMapping(value = "/admin/quizes/{id}")
    public ModelAndView addQuestion(@PathVariable("id") int id) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        Quiz quiz = quizService.findQuizById(id);
        modelAndView.addObject("quizName", "Add question to " + quiz.getQuizName());
        modelAndView.addObject("quizId", id);
        modelAndView.addObject("qp", new QuestionPrototype(10));
        modelAndView.setViewName("admin/add");
        return modelAndView;
    }

    @PostMapping(value = "/admin/quizes/{id}")
    public ModelAndView addQuestion(@PathVariable("id") int quizId, @ModelAttribute("qp") QuestionPrototype qp,
                                   BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());

        Quiz quiz = quizService.findQuizById(quizId);
        Question question = qp.generateQuestion();
        question.setQuiz(quiz);
        quizService.saveQuestion(question);
        modelAndView.addObject("quizes", quizService.findAvailableQuizes(user));
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    private static class QuestionPrototype {
        private String questionText;
        private boolean singleAnswer;
        private List<String> options;
        private List<Boolean> correctAnswers;

        public QuestionPrototype(int n) {
            questionText = "";
            singleAnswer = false;
            options = new ArrayList<>();
            correctAnswers = new ArrayList<>();
            for(int i = 0; i<n; i++) {
                options.add("");
                correctAnswers.add(false);
            }
        }

        public Question generateQuestion() {
            List<String> correct = new ArrayList<>();
            int n;
            for(n = 0; n<options.size() && !options.get(n).isEmpty(); n++);

            for(int i = 0; i<n && i < correctAnswers.size(); i++) {
                if(correctAnswers.get(i) != null && correctAnswers.get(i)) {
                    correct.add(Integer.toString(i));
                }
            }

            return Question.builder()
                    .questionText(questionText)
                    .singleAnswer(singleAnswer)
                    .options(String.join(Question.SEPARATOR, options))
                    .correctAnswers(String.join(Question.SEPARATOR, correct)).build();
        }
    }
}
