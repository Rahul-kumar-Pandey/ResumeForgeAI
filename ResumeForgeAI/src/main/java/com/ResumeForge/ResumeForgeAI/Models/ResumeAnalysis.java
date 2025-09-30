package com.ResumeForge.ResumeForgeAI.Models;

import java.util.List;

public class ResumeAnalysis {
	private String analysis;
    private List<String> interviewQuestions;
    private String optimizedResume;
    private double matchScore;
    private List<String> skillGaps;
    private List<String> matchedKeywords;
    
    // Constructors, getters, and setters
    public ResumeAnalysis() {}
    
    public ResumeAnalysis(String analysis, List<String> interviewQuestions, 
                         String optimizedResume, double matchScore, 
                         List<String> skillGaps, List<String> matchedKeywords) {
        this.analysis = analysis;
        this.interviewQuestions = interviewQuestions;
        this.optimizedResume = optimizedResume;
        this.matchScore = matchScore;
        this.skillGaps = skillGaps;
        this.matchedKeywords = matchedKeywords;
    }
    
    // Getters and setters...
    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }
    
    public List<String> getInterviewQuestions() { return interviewQuestions; }
    public void setInterviewQuestions(List<String> interviewQuestions) { this.interviewQuestions = interviewQuestions; }
    
    public String getOptimizedResume() { return optimizedResume; }
    public void setOptimizedResume(String optimizedResume) { this.optimizedResume = optimizedResume; }
    
    public double getMatchScore() { return matchScore; }
    public void setMatchScore(double matchScore) { this.matchScore = matchScore; }
    
    public List<String> getSkillGaps() { return skillGaps; }
    public void setSkillGaps(List<String> skillGaps) { this.skillGaps = skillGaps; }
    
    public List<String> getMatchedKeywords() { return matchedKeywords; }
    public void setMatchedKeywords(List<String> matchedKeywords) { this.matchedKeywords = matchedKeywords; }
}
