package ru.asmisloff.quiz.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asmisloff.quiz.dto.AnswerVariantDto;
import ru.asmisloff.quiz.dto.QuestionDto;
import ru.asmisloff.quiz.entity.Question;
import ru.asmisloff.quiz.repository.AnswerRepository;
import ru.asmisloff.quiz.repository.QuestionRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerVariantService answerVariantService;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ModelMapper modelMapper;

    public QuestionDto save(QuestionDto questionDto) {
        return modelMapper.map(
                questionRepository.save(modelMapper.map(questionDto, Question.class)),
                QuestionDto.class
        );
    }

    public List<QuestionDto> saveAll(List<QuestionDto> questionDtos) {
        List<Question> questions = questionRepository.saveAll(
                questionDtos.stream()
                        .map(q -> modelMapper.map(q, Question.class))
                        .collect(Collectors.toUnmodifiableList())
        );

        questionDtos = questions.stream()
                .map(q -> modelMapper.map(q, QuestionDto.class))
                .collect(Collectors.toUnmodifiableList());

        for (QuestionDto questionDto : questionDtos) {
            List<AnswerVariantDto> answerVariantDtos = questionDto.getAnswerVariants();
            answerVariantDtos.forEach(avd -> avd.setQuestionId(questionDto.getId()));
            answerVariantDtos = answerVariantService.saveAll(answerVariantDtos);
            questionDto.setAnswerVariants(answerVariantDtos);
        }

        return questionDtos;
    }

    public void deleteAllByQuizIdSkipIds(Long quizId, List<Long> idsToSkip) {
        questionRepository.deleteAllByQuizIdSkipIds(quizId, idsToSkip);
    }

    @Transactional
    public void deleteById(Long id) {
        answerRepository.deleteAllByQuestionId(id);
        questionRepository.deleteById(id);
    }

}
