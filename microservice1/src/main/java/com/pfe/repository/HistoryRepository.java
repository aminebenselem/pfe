package com.pfe.repository;

import com.pfe.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findByAttemptDateBetween(LocalDateTime start, LocalDateTime end);

    long countByAttemptDateBetween(LocalDateTime start, LocalDateTime end);

    long countByPassedIsTrueAndAttemptDateBetween(LocalDateTime start, LocalDateTime end);
}
