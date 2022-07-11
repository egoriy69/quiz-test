package com.example.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quiz")
public class Quiz {
    public static final String SEPARATOR = "@@";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "quiz_id")
    private int id;

    @Column(name = "quiz_name")
    @NotEmpty(message = "*Please provide a quiz name")
    private String quizName;

    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;
}
