package ru.asmisloff.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asmisloff.quiz.entity.AnswerVariant;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnswerVariantRepository extends JpaRepository<AnswerVariant, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM AnswerVariant av WHERE av.question.id = ?1 AND av.id NOT IN ?2")
    void deleteAllByQuestionIdSkipIds(Long id, List<Long> newAnswerVariantIds);
}
