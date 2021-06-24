package ru.asmisloff.quiz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asmisloff.quiz.dto.AnswerAdditionDto;
import ru.asmisloff.quiz.dto.AnswerDto;
import ru.asmisloff.quiz.dto.UserAnswerReportEntry;
import ru.asmisloff.quiz.service.AnswerService;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    @Autowired
    AnswerService answerService;

    @PostMapping("/add-answer")
    public AnswerDto addAnswer(@RequestBody AnswerAdditionDto answerAdditionDto) {
        AnswerDto answerDto = answerService.addAnswer(answerAdditionDto);
        return answerDto;
    }

    @GetMapping("/get-report/{userId}")
    public List<UserAnswerReportEntry> getReport(@PathVariable("userId") Long userId) {
        return answerService.getReport(userId);
    }

}
