package com.pfe.dto;

public class DashboardStatsDTO {
    private double averageScore;
    private double averageScoreChange;
    private double completionRate;
    private double completionRateChange;
    private long assessmentsTaken;
    private long assessmentsTakenChange;
    private double passRate;

    public double getPassRateChange() {
        return passRateChange;
    }

    public void setPassRateChange(double passRateChange) {
        this.passRateChange = passRateChange;
    }

    public double getPassRate() {
        return passRate;
    }

    public void setPassRate(double passRate) {
        this.passRate = passRate;
    }

    public long getAssessmentsTakenChange() {
        return assessmentsTakenChange;
    }

    public void setAssessmentsTakenChange(long assessmentsTakenChange) {
        this.assessmentsTakenChange = assessmentsTakenChange;
    }

    public long getAssessmentsTaken() {
        return assessmentsTaken;
    }

    public void setAssessmentsTaken(long assessmentsTaken) {
        this.assessmentsTaken = assessmentsTaken;
    }

    public double getCompletionRateChange() {
        return completionRateChange;
    }

    public void setCompletionRateChange(double completionRateChange) {
        this.completionRateChange = completionRateChange;
    }

    public double getCompletionRate() {
        return completionRate;
    }

    public void setCompletionRate(double completionRate) {
        this.completionRate = completionRate;
    }

    public double getAverageScoreChange() {
        return averageScoreChange;
    }

    public void setAverageScoreChange(double averageScoreChange) {
        this.averageScoreChange = averageScoreChange;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    private double passRateChange;

}
