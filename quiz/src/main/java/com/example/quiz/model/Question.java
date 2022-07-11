package com.example.quiz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "question")
public class Question {
    public static final String SEPARATOR = "#";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private int id;

    @Column(name = "question_text")
    @NotEmpty(message = "*Please provide a question text")
    private String questionText;

    @Column(name = "single_answer")
    private boolean singleAnswer;

    @Column(name = "options")
    private String options;

    @Column(name = "correct")
    private String correctAnswers;

    @ManyToOne
    @JoinColumn(name="quiz_id")
    private Quiz quiz;

    @Override
    public int hashCode() {
        return Objects.hash(questionText, quiz == null ? 0 : quiz.getId());
    }
}
