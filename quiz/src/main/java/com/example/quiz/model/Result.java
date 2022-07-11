package com.example.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "result")
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "result_id")
    private int id;

    @OneToOne
    @JoinColumn(name = "users", referencedColumnName = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "quizes", referencedColumnName = "quiz_id")
    private Quiz quiz;

    @Column(name = "score")
    private int score;

    @Column(name = "dateTime")
    private Date dateTime;

    @Column(name = "answers")
    private String answers;
}
