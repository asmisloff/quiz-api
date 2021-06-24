package ru.asmisloff.quiz.dto;

import lombok.*;

import java.util.List;

@Data
public class AnswerAdditionDto {

    private Long id;
    private Long userId;
    private String text;
    private Long questionId;
    private List<Long> answerVariantIds;

}
