package com.pfe.dto;

import java.util.List;

public class UserAnswerDto {
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public List<Long> getSelectedOptionIds() {
        return selectedOptionIds;
    }

    public void setSelectedOptionIds(List<Long> selectedOptionIds) {
        this.selectedOptionIds = selectedOptionIds;
    }

    private Long questionId;
    private List<Long> selectedOptionIds;

    // Constructors

}

