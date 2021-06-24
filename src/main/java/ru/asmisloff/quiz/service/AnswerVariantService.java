package ru.asmisloff.quiz.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.asmisloff.quiz.dto.AnswerVariantDto;
import ru.asmisloff.quiz.entity.AnswerVariant;
import ru.asmisloff.quiz.repository.AnswerVariantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerVariantService {

    @Autowired
    AnswerVariantRepository answerVariantRepository;

    @Autowired
    ModelMapper modelMapper;

    public void deleteAllByQuestionIdSkipIds(Long id, List<Long> answerIdsToSkip) {
        answerVariantRepository.deleteAllByQuestionIdSkipIds(id, answerIdsToSkip);
    }

    public List<AnswerVariantDto> saveAll(List<AnswerVariantDto> answerVariantDtos) {
        List<AnswerVariant> answerVariants = answerVariantDtos.stream()
                .map(dto -> modelMapper.map(dto, AnswerVariant.class))
                .collect(Collectors.toUnmodifiableList());

        answerVariants = answerVariantRepository.saveAll(answerVariants);

        return answerVariants.stream()
                .map(av -> modelMapper.map(av, AnswerVariantDto.class))
                .collect(Collectors.toUnmodifiableList());
    }

}
