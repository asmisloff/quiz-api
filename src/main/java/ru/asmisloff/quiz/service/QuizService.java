package ru.asmisloff.quiz.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asmisloff.quiz.dto.AnswerVariantDto;
import ru.asmisloff.quiz.dto.QuestionDto;
import ru.asmisloff.quiz.dto.QuizDto;
import ru.asmisloff.quiz.entity.Question;
import ru.asmisloff.quiz.entity.Quiz;
import ru.asmisloff.quiz.repository.QuestionRepository;
import ru.asmisloff.quiz.repository.QuizRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired QuestionService questionService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AnswerVariantService answerVariantService;

    public QuizDto create(QuizDto quizDto) {
        if (quizDto.getId() != null) {
            quizDto.setId(null);
        }
        Quiz quiz = quizRepository.save(modelMapper.map(quizDto, Quiz.class));
        quizDto.getQuestions().forEach(q -> q.setQuizId(quiz.getId()));
        quizDto.setId(quiz.getId());
        List<QuestionDto> questionDtos = questionService.saveAll(quizDto.getQuestions());
        quizDto.setQuestions(questionDtos);
        return quizDto;
    }

    public QuizDto update(QuizDto quizDto) {
        if (quizDto.getId() == null) {
            throw new RuntimeException("Can't update quiz with id = null");
        }
        Quiz quiz = quizRepository.save(modelMapper.map(quizDto, Quiz.class));
        quizDto.getQuestions().forEach(q -> q.setQuizId(quiz.getId()));

        List<Long> newQuestionIds = quizDto.getQuestions().stream()
                .map(QuestionDto::getId)
                .filter(id -> id != null)
                .collect(Collectors.toUnmodifiableList());
        System.out.println(newQuestionIds);

        questionService.deleteAllByQuizIdSkipIds(quizDto.getId(),newQuestionIds);
        List<QuestionDto> questionDtos = questionService.saveAll(quizDto.getQuestions());
        quizDto.setQuestions(questionDtos);
        return quizDto;
    }

    public QuizDto getById(Long id) {
        Quiz quiz = quizRepository.findById(id).orElse(null);
        if (quiz == null) {
            return null;
        }
        return modelMapper.map(quiz, QuizDto.class);
    }

    public List<Quiz> getActive() {
        LocalDate today = LocalDate.now();
        return quizRepository.findByDate(today);
    }

    public void deleteById(Long id) {
        quizRepository.deleteById(id);
    }

    public QuizDto addQuestion(Long quizId, QuestionDto questionDto) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null) {
            throw new RuntimeException(String.format("Quiz with id = %s not found in DB", quizId));
        }

        questionDto.setQuizId(quizId);
        questionDto.setId(null);
        questionService.save(questionDto);
        QuizDto quizDto = getById(quizId);
        System.out.println(quizDto);
        return quizDto;
    }

    public QuizDto updateQuestion(QuestionDto questionDto) {
        if (questionDto.getId() == null) {
            throw new RuntimeException("Can't update question with id = null");
        }
        Question question = questionRepository.findById(questionDto.getId()).orElse(null);
        if (question == null) {
            throw new RuntimeException(String.format("Can't update question with id = %s. It doesn't exist in DB.", questionDto.getId()));
        }

        List<Long> newAnswerVariantIds = questionDto.getAnswerVariants().stream()
                .map(AnswerVariantDto::getId)
                .filter(id -> id != null)
                .collect(Collectors.toUnmodifiableList());
        answerVariantService.deleteAllByQuestionIdSkipIds(question.getId(), newAnswerVariantIds);

        questionDto.getAnswerVariants().forEach(av -> av.setQuestionId(questionDto.getId()));
        List<AnswerVariantDto> answerVariantDtos = answerVariantService.saveAll(questionDto.getAnswerVariants());

        questionDto.setAnswerVariants(answerVariantDtos);
        questionDto.setQuizId(question.getQuiz().getId());
        questionService.save(questionDto);
        return getById(questionDto.getQuizId());
    }

}
