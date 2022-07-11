package com.example.quiz.service;

import com.example.quiz.model.Quiz;
import com.example.quiz.model.Result;
import com.example.quiz.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResultService {
    private ResultRepository resultRepository;

    @Autowired
    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void saveResult(Result result) {
        resultRepository.save(result);
    }

    public List<Result> findAllResults() {
        return resultRepository.findAll(Sort.by("dateTime").descending());
    }

    public Result findResultById(int id) {
        return resultRepository.findResultById(id);
    }
}
