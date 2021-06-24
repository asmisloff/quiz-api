package ru.asmisloff.quiz.dto;

import lombok.Data;

@Data
public class AnswerVariantDto {

    private Long id;
    private String text;
    private Long questionId;

}
