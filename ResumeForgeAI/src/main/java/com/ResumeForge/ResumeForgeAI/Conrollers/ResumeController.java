package com.ResumeForge.ResumeForgeAI.Conrollers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ResumeForge.ResumeForgeAI.Models.ResumeAnalysis;
import com.ResumeForge.ResumeForgeAI.Services.PdfTextExtractorService;
import com.ResumeForge.ResumeForgeAI.Services.ResumeAnalysisService;

@RestController
@RequestMapping("/api/resume")
@CrossOrigin(origins = "*")
public class ResumeController {
	@Autowired
	private  ResumeAnalysisService analysisService;
	@Autowired
    private  PdfTextExtractorService pdfService;
    
//    public ResumeController(ResumeAnalysisService analysisService, PdfTextExtractorService pdfService) {
//        this.analysisService = analysisService;
//        this.pdfService = pdfService;
//    }
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeResume(
            @RequestParam("resume") MultipartFile resume,
            @RequestParam("jobDescription") String jobDescription) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validate inputs
            if (resume.isEmpty()) {
                response.put("success", false);
                response.put("error", "Resume file is required");
                return ResponseEntity.badRequest().body(response);
            }

            if (jobDescription == null || jobDescription.trim().isEmpty()) {
                response.put("success", false);
                response.put("error", "Job description is required");
                return ResponseEntity.badRequest().body(response);
            }

            // Extract text from PDF
            String resumeText = pdfService.extractTextFromPdf(resume);
            
            // Analyze resume
            ResumeAnalysis analysis = analysisService.analyzeResume(resumeText, jobDescription);
            
            // Build success response
            response.put("success", true);
            response.put("data", analysis);
            response.put("fileName", resume.getOriginalFilename());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Resume Analyzer API is running!");
    }
}
