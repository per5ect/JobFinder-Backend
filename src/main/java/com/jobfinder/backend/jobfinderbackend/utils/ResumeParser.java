package com.jobfinder.backend.jobfinderbackend.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URL;

@Component
public class ResumeParser {
    public String extractTextFromPDF(String resumeUrl){
        try(InputStream inputStream = new URL(resumeUrl).openStream();
            PDDocument document = PDDocument.load(inputStream)){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        } catch (Exception e) {
            throw new RuntimeException("Parse Error:", e);
        }
    }
}
