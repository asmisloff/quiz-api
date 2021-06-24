package ru.asmisloff.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asmisloff.quiz.entity.Quiz;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query("FROM Quiz q WHERE q.startDate <= ?1 AND q.endDate >= ?1")
    List<Quiz> findByDate(LocalDate date);

}
