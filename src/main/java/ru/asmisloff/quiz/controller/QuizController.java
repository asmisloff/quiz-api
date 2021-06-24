package ru.asmisloff.quiz.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.asmisloff.quiz.dto.QuestionDto;
import ru.asmisloff.quiz.dto.QuizDto;
import ru.asmisloff.quiz.service.QuestionService;
import ru.asmisloff.quiz.service.QuizService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quiz")
public class QuizController {

    @Autowired
    QuizService quizService;

    @Autowired
    QuestionService questionService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public QuizDto create(@RequestBody QuizDto quizDto) {
        return quizService.create(quizDto);
    }

    @PostMapping("/update")
    public QuizDto update(@RequestBody QuizDto quizDto) {
        return quizService.update(quizDto);
    }

    @GetMapping("/get/active")
    public List<QuizDto> getActiveQuizes() {
        return  quizService.getActive().stream()
                .map(q -> modelMapper.map(q, QuizDto.class))
                .collect(Collectors.toUnmodifiableList());
    }

    @DeleteMapping("delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        quizService.deleteById(id);
    }

    @PostMapping("/add-question/{quizId}")
    public QuizDto addQuestion(@PathVariable("quizId") Long quizId, @RequestBody QuestionDto questionDto) {
        return quizService.addQuestion(quizId, questionDto);
    }

    @PostMapping("update-question")
    public QuizDto updateQuestion(@RequestBody QuestionDto questionDto) {
        return quizService.updateQuestion(questionDto);
    }

    @DeleteMapping("delete-question/{id}")
    public void deleteQuestion(@PathVariable("id") Long id) {
        questionService.deleteById(id);
    }

}
