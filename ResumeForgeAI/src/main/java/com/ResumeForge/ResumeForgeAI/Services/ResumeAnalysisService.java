package com.ResumeForge.ResumeForgeAI.Services;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ResumeForge.ResumeForgeAI.Models.ResumeAnalysis;

@Service
public class ResumeAnalysisService {
	@Autowired
	 private GeminiService geminiService;
	@Autowired
	    private PdfTextExtractorService pdfService;

//	    public ResumeAnalysisService(GeminiService geminiService, PdfTextExtractorService pdfService) {
//	        this.geminiService = geminiService;
//	        this.pdfService = pdfService;
//	    }

	    public ResumeAnalysis analyzeResume(String resumeText, String jobDescription) {
	        try {
	            // Generate analysis using Gemini
	            String analysis = geminiService.generateContent(
	                buildAnalysisPrompt(resumeText, jobDescription)
	            );

	            // Generate interview questions
	            String questionsResponse = geminiService.generateContent(
	                buildQuestionsPrompt(resumeText, jobDescription)
	            );
	            List<String> interviewQuestions = parseQuestions(questionsResponse);

	            // Generate optimized resume
	            String optimizedResume = geminiService.generateContent(
	                buildOptimizationPrompt(resumeText, jobDescription)
	            );

	            // Calculate match score (simplified)
	            double matchScore = calculateMatchScore(resumeText, jobDescription);
	            
	            // Extract skill gaps and keywords
	            List<String> skillGaps = extractSkillGaps(resumeText, jobDescription);
	            List<String> matchedKeywords = extractMatchedKeywords(resumeText, jobDescription);

	            return new ResumeAnalysis(analysis, interviewQuestions, optimizedResume, 
	                                   matchScore, skillGaps, matchedKeywords);

	        } catch (Exception e) {
	            throw new RuntimeException("Resume analysis failed: " + e.getMessage());
	        }
	    }

	    private String buildAnalysisPrompt(String resumeText, String jobDescription) {
	        return String.format("""
	            Analyze this resume against the job description and provide specific improvement suggestions.
	            
	            JOB DESCRIPTION:
	            %s
	            
	            RESUME:
	            %s
	            
	            Provide concrete suggestions focusing on:
	            1. Missing skills that should be added
	            2. Better phrasing of experiences
	            3. Quantifiable achievements to include
	            4. Formatting improvements
	            5. Keywords to incorporate
	            
	            Be specific and provide examples.
	            """, jobDescription, resumeText);
	    }

	    private String buildQuestionsPrompt(String resumeText, String jobDescription) {
	        return String.format("""
	            Generate 5 technical interview questions for this candidate.
	            
	            JOB: %s
	            RESUME: %s
	            
	            Return only the questions as a numbered list, one per line.
	            """, jobDescription, resumeText);
	    }

	    private String buildOptimizationPrompt(String resumeText, String jobDescription) {
	        return String.format("""
	            Optimize this resume for the job description.
	            
	            JOB: %s
	            RESUME: %s
	            
	            Return only the optimized resume text with:
	            - Better keywords from job description
	            - Improved action verbs
	            - Quantifiable achievements
	            - Professional summary
	            
	            Keep the original structure but enhance the content.
	            """, jobDescription, resumeText);
	    }

	    private List<String> parseQuestions(String questionsText) {
	        return Arrays.stream(questionsText.split("\n"))
	                .filter(line -> line.matches("^\\d+\\..*"))
	                .map(line -> line.replaceFirst("^\\d+\\.\\s*", ""))
	                .toList();
	    }

	    private double calculateMatchScore(String resumeText, String jobDescription) {
	        String[] jobKeywords = extractKeywords(jobDescription);
	        String resumeLower = resumeText.toLowerCase();
	        
	        long matchedCount = Arrays.stream(jobKeywords)
	                .filter(keyword -> resumeLower.contains(keyword.toLowerCase()))
	                .count();
	        
	        return (double) matchedCount / jobKeywords.length * 100;
	    }

	    private String[] extractKeywords(String text) {
	        return text.toLowerCase()
	                .replaceAll("[^a-zA-Z0-9\\s]", "")
	                .split("\\s+");
	    }

	    private List<String> extractSkillGaps(String resumeText, String jobDescription) {
	        // Simplified implementation - in real app, use AI for this
	        return List.of("AWS Cloud", "Docker", "Kubernetes"); // Example
	    }

	    private List<String> extractMatchedKeywords(String resumeText, String jobDescription) {
	        // Simplified implementation
	        return List.of("Java", "Spring Boot", "Microservices", "REST API"); // Example
	    }
}
