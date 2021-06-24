package ru.asmisloff.quiz.dto;

import lombok.Data;
import ru.asmisloff.quiz.entity.QuestionType;

import java.util.List;

@Data
public class QuestionDto {

    private Long id;
    private String text;
    private QuestionType questionType;
    private Long quizId;
    private List<AnswerVariantDto> answerVariants;

}
