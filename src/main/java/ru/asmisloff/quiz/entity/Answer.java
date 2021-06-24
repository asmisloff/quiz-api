package ru.asmisloff.quiz.entity;

import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answers")
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToMany
    @JoinTable(
            name = "answer_answer_variants",
            joinColumns = {@JoinColumn(name = "answer_id")},
            inverseJoinColumns = {@JoinColumn(name = "answer_variant_id")}
    )
    private List<AnswerVariant> answerVariants = new ArrayList<>();

}
