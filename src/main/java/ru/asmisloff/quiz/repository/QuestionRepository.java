package ru.asmisloff.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asmisloff.quiz.entity.Question;
import ru.asmisloff.quiz.entity.Quiz;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    void deleteAllByQuizId(Long id);

    List<Question> findAllByQuizId(Long quizId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Question q WHERE q.quiz.id = ?1 AND q.id NOT IN ?2")
    void deleteAllByQuizIdSkipIds(Long quizId, List<Long> idsToSkip);
}
