package ru.asmisloff.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnswerDto {

    private Long id;
    private Long userId;
    private String text;
    private Long questionId;
    private List<AnswerVariantDto> answerVariants;

}
