package ru.asmisloff.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asmisloff.quiz.dto.AnswerDto;
import ru.asmisloff.quiz.entity.Answer;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    List<Answer> findAllByUserId(Long userId);

    @Transactional
    void deleteAllByQuestionId(Long id);

}
