package com.pfe.controller;

import com.pfe.dto.QuizResultDto;
import com.pfe.dto.UserAnswerDto;
import com.pfe.entity.*;
import com.pfe.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class QcmController {

    @Autowired
    private QcmRep qcmRepository;
    @Autowired
    private OptionsRepository optionsRepository;
    @Autowired
    private QuestionsRepository questionsRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/qcm")
    public ResponseEntity<String> createQcm(@RequestBody Qcm qcmDTO) {
        Qcm qcm = new Qcm();
        qcm.setTitle(qcmDTO.getTitle());
        qcm.setDuration(qcmDTO.getDuration());
        qcm.setPassingScore(qcmDTO.getPassingScore());
         qcm.setDescription(qcmDTO.getDescription());
        List<Question> questions = new ArrayList<>();

        for (Question questionDTO : qcmDTO.getQuestions()) {
            Question question = new Question();
            question.setContent(questionDTO.getContent());
            question.setQcm(qcm);

            List<Options> options = new ArrayList<>();
            for (Options optionDTO : questionDTO.getOptions()) {
                Options option = new Options();
                option.setValue(optionDTO.getValue());
                option.setCorrect(optionDTO.isCorrect());
                option.setQuestion(question);
                options.add(option);
            }

            question.setOptions(options);
            questions.add(question);
        }

        qcm.setQuestions(questions);
        qcmRepository.save(qcm); // thanks to cascade, questions and options are saved too!

        return ResponseEntity.ok("QCM saved successfully!");
    }
    @GetMapping("/qcm")
    public List<Qcm> getQcm() {
        return (List<Qcm>) this.qcmRepository.findAll();
    }
    @DeleteMapping("/qcm/{id}")
    public ResponseEntity<String> deleteQcm(@PathVariable Long id) {
        Optional<Qcm> qcmOptional = qcmRepository.findById(id);

        if (qcmOptional.isPresent()) {
            qcmRepository.deleteById(id);
            return ResponseEntity.ok("QCM deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("QCM with id " + id + " not found.");
        }
    }
    @GetMapping("/qcm/{id}")
    public ResponseEntity<Qcm> getQcmById(@PathVariable Long id) {
        Optional<Qcm> qcmOptional = qcmRepository.findById(id);

        if (qcmOptional.isPresent()) {
            return ResponseEntity.ok(qcmOptional.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/quiz/submit/{quizId}")
    public ResponseEntity<QuizResultDto> submitQuiz(@PathVariable Long quizId, @RequestBody List<UserAnswerDto> userAnswers) {
        Qcm quiz = qcmRepository.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        int totalQuestions = quiz.getQuestions().size();
        int correctAnswers = 0;

        for (UserAnswerDto userAnswer : userAnswers) {
            Question question = questionsRepository.findById(userAnswer.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            List<Options> correctOptions = question.getOptions()
                    .stream()
                    .filter(Options::isCorrect)
                    .collect(Collectors.toList());

            List<Long> correctOptionIds = correctOptions.stream()
                    .map(Options::getId)
                    .collect(Collectors.toList());

            List<Long> submittedOptionIds = userAnswer.getSelectedOptionIds();

            if (new HashSet<>(submittedOptionIds).equals(new HashSet<>(correctOptionIds))) {
                correctAnswers++;
            }
        }

        int score = (int) ((correctAnswers / (double) totalQuestions) * 100);
        boolean passed = score >= quiz.getPassingScore();

        // ---------------- Save to History ----------------
        History history = new History();
        history.setScore((double) score);
        history.setPassed(passed);
        history.setAttemptDate(LocalDateTime.now());
        history.setQcmHist(quiz);

        // Get the current authenticated user
        User currentUser = getCurrentAuthenticatedUser(); // You have to implement this method
        history.setUser(currentUser);

        historyRepository.save(history);
        // ---------------------------------------------------

        QuizResultDto result = new QuizResultDto(score, passed);
        return ResponseEntity.ok(result);
    }


    public User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
