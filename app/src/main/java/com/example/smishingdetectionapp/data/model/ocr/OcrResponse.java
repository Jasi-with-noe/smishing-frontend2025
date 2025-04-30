package com.example.smishingdetectionapp.data.model.ocr;

import java.util.List;
public class OcrResponse {
    private String status;
    private String extractedText;
    private String riskLevel;
    private List<String> matchedKeywords;

    //–– Getters only (immutability) ––
    public String getStatus()            { return status; }
    public String getExtractedText()     { return extractedText; }
    public String getRiskLevel()         { return riskLevel; }
    public List<String> getMatchedKeywords() { return matchedKeywords; }
}
