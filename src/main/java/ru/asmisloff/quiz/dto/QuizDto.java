package ru.asmisloff.quiz.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
public class QuizDto {

    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    List<QuestionDto> questions;

}
