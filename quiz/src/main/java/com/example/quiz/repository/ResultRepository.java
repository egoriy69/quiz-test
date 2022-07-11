package com.example.quiz.repository;

import com.example.quiz.model.Quiz;
import com.example.quiz.model.Result;
import com.example.quiz.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository extends JpaRepository<Result, Integer> {
    @Query("SELECT r.quiz FROM Result AS r WHERE r.user = ?1 GROUP BY r.quiz HAVING COUNT(r.quiz) > 1")
    List<Quiz> findNotAvailable(User u);
    Result findResultById(int id);
}
