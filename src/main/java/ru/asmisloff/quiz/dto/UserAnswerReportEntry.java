package ru.asmisloff.quiz.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserAnswerReportEntry {

    Long quizId;
    List<AnswerDto> answers;

}
