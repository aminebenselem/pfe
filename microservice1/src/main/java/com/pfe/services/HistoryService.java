package com.pfe.services;

import com.pfe.dto.DashboardStatsDTO;
import com.pfe.entity.History;
import com.pfe.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistoryService {

    @Autowired
    private HistoryRepository historyRepository;

    public DashboardStatsDTO getDashboardStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDate firstDayOfThisMonth = now.withDayOfMonth(1).toLocalDate();
        LocalDateTime startOfThisMonth = firstDayOfThisMonth.atStartOfDay();
        LocalDateTime startOfNextMonth = firstDayOfThisMonth.plusMonths(1).atStartOfDay();

        LocalDate firstDayOfLastMonth = firstDayOfThisMonth.minusMonths(1);
        LocalDateTime startOfLastMonth = firstDayOfLastMonth.atStartOfDay();
        LocalDateTime endOfLastMonth = startOfThisMonth;

        // Fetch data
        List<History> thisMonthHistories = historyRepository.findByAttemptDateBetween(startOfThisMonth, startOfNextMonth);
        List<History> lastMonthHistories = historyRepository.findByAttemptDateBetween(startOfLastMonth, endOfLastMonth);

        // Current month calculations
        double currentAverageScore = thisMonthHistories.stream()
                .mapToDouble(History::getScore)
                .average()
                .orElse(0.0);

        long currentAssessments = thisMonthHistories.size();
        long currentPassed = thisMonthHistories.stream()
                .filter(History::isPassed)
                .count();

        double currentCompletionRate = currentAssessments > 0 ? 100.0 : 0.0;
        double currentPassRate = currentAssessments > 0 ? (currentPassed * 100.0 / currentAssessments) : 0.0;

        // Last month calculations
        double lastAverageScore = lastMonthHistories.stream()
                .mapToDouble(History::getScore)
                .average()
                .orElse(0.0);

        long lastAssessments = lastMonthHistories.size();
        long lastPassed = lastMonthHistories.stream()
                .filter(History::isPassed)
                .count();

        double lastCompletionRate = lastAssessments > 0 ? 100.0 : 0.0;
        double lastPassRate = lastAssessments > 0 ? (lastPassed * 100.0 / lastAssessments) : 0.0;

        // Calculate changes
        double averageScoreChange = lastAverageScore == 0 ? 0 : ((currentAverageScore - lastAverageScore) / lastAverageScore) * 100;
        double completionRateChange = lastCompletionRate == 0 ? 0 : ((currentCompletionRate - lastCompletionRate) / lastCompletionRate) * 100;
        long assessmentsTakenChange = currentAssessments - lastAssessments;
        double passRateChange = lastPassRate == 0 ? 0 : ((currentPassRate - lastPassRate) / lastPassRate) * 100;

        // Create and return DTO
        DashboardStatsDTO stats = new DashboardStatsDTO();
        stats.setAverageScore(currentAverageScore);
        stats.setAverageScoreChange(averageScoreChange);
        stats.setCompletionRate(currentCompletionRate);
        stats.setCompletionRateChange(completionRateChange);
        stats.setAssessmentsTaken(currentAssessments);
        stats.setAssessmentsTakenChange(assessmentsTakenChange);
        stats.setPassRate(currentPassRate);
        stats.setPassRateChange(passRateChange);

        return stats;
    }
}

