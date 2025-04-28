package com.pfe.dto;

public class QuizResultDto {
    private int score;
    private boolean passed;

    // Constructors
    public QuizResultDto() {}

    public QuizResultDto(int score, boolean passed) {
        this.score = score;
        this.passed = passed;
    }

    // Getters and Setters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }
}

