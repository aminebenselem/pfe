package com.pfe.controller;

import com.pfe.dto.DashboardStatsDTO;
import com.pfe.entity.History;
import com.pfe.entity.User;
import com.pfe.repository.HistoryRepository;
import com.pfe.repository.UserRepository;
import com.pfe.services.HistoryService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HistoryController {
    private final HistoryRepository historyRepository;
    private  final UserRepository userRepository;
    public HistoryController(HistoryRepository historyRepository, UserRepository userRepository, HistoryService historyService) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
        this.historyService = historyService;
    }
    private final HistoryService historyService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        DashboardStatsDTO stats = historyService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }
    @GetMapping("history")
    public List<History> getHistory() {
        return historyRepository.findAll();
    }
    @Transactional
    @GetMapping("/myhistory")
    public List<History> getMyHistory() {
        // Get the user from security
        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Reload the user from database to get a managed entity
        User user = userRepository.findById(authenticatedUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Now you can safely access histories
        return user.getHistories();
    }


}
