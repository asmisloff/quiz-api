package ru.asmisloff.quiz.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asmisloff.quiz.dto.AnswerAdditionDto;
import ru.asmisloff.quiz.dto.AnswerDto;
import ru.asmisloff.quiz.dto.UserAnswerReportEntry;
import ru.asmisloff.quiz.entity.Answer;
import ru.asmisloff.quiz.entity.AnswerVariant;
import ru.asmisloff.quiz.entity.Question;
import ru.asmisloff.quiz.repository.AnswerRepository;
import ru.asmisloff.quiz.repository.AnswerVariantRepository;
import ru.asmisloff.quiz.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerVariantRepository answerVariantRepository;

    @Autowired
    ModelMapper modelMapper;

    public AnswerDto addAnswer(AnswerAdditionDto answerAdditionDto) {
        System.out.println("answerAdditionDto: " + answerAdditionDto);
        if (answerAdditionDto.getUserId() == null) {
            throw new RuntimeException("userId must present in answer");
        }
        if (answerAdditionDto.getQuestionId() == null) {
            throw new RuntimeException("userId must present in answer");
        }

        Answer answer = new Answer();
        answer.setId(null);
        answer.setUserId(answerAdditionDto.getUserId());
        Question question = questionRepository.findById(answerAdditionDto.getQuestionId()).orElse(null);
        if (question == null) {
            throw new RuntimeException(String.format("Question with id = %d not found in DB", answerAdditionDto.getQuestionId()));
        }
        answer.setQuestion(question);
        List<Long> answerVariantIds = answerAdditionDto.getAnswerVariantIds();

        switch (question.getQuestionType()) {
            case TEXT:
                if (answerVariantIds.size() != 0) {
                    throw new RuntimeException("Question type is TEXT. No answer variants available.");
                }
                answer.setText(answerAdditionDto.getText());
                break;
            case ONE_CHOICE:
                if (answerVariantIds.size() != 1) {
                    throw new RuntimeException("Question type is ONE_CHOICE. Must be provided one and only one answer variant.");
                }
                answer.setText(null);
                break;
            case MULTIPLE_CHOICES:
                if (answerVariantIds.size() < 1) {
                    throw new RuntimeException("Question type is MULTIPLE_CHOICE. Must be provided at least one answer variant.");
                }
                answer.setText(null);
                break;
        }

        for (Long id : answerVariantIds) {
            AnswerVariant av = answerVariantRepository.findById(id).orElse(null);
            if (av == null) {
                throw new RuntimeException(String.format("Answer variant with id = %d not found in DB", id));
            }
            answer.getAnswerVariants().add(av);
        }

        answer = answerRepository.save((answer));

        return modelMapper.map(answer, AnswerDto.class);
    }

    public List<UserAnswerReportEntry> getReport(Long userId) {
        List<Answer> answers = answerRepository.findAllByUserId(userId);
        Map<Long, List<Answer>> answersGroupedByQuizId = new HashMap<>();
        for (Answer answer : answers) {
            Long quizId = answer.getQuestion().getQuiz().getId();
            if (!answersGroupedByQuizId.containsKey(quizId)) {
                answersGroupedByQuizId.put(quizId, new ArrayList<>());
            }
            answersGroupedByQuizId.get(quizId).add(answer);
        }

        List<UserAnswerReportEntry> report = new ArrayList<>();
        for (Long id : answersGroupedByQuizId.keySet()) {
            UserAnswerReportEntry reportEntry = new UserAnswerReportEntry();
            reportEntry.setQuizId(id);
            reportEntry.setAnswers(
                    answersGroupedByQuizId.get(id).stream()
                            .map(a -> modelMapper.map(a, AnswerDto.class))
                            .collect(Collectors.toUnmodifiableList())
            );
            report.add(reportEntry);
        }

        return report;
    }

}
