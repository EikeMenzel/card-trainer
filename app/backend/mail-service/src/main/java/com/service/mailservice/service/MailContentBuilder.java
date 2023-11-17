package com.service.mailservice.service;

import com.service.mailservice.model.MailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class MailContentBuilder {
    private final Logger logger =  LoggerFactory.getLogger(MailContentBuilder.class);

    public String getContent(MailType mailType, String token) {
        switch (mailType) {
            case VERIFICATION -> {
                return getContentFromFile("static/mail_verification.html").replace("${verificationUrl}", buildVerificationUrl(token));
            }
            default -> {
                return "";
            }
        }
    }

    private String getContentFromFile(String filePath) {
        try {
            return Files.readString(new ClassPathResource(filePath).getFile().toPath());
        } catch (IOException e) {
            logger.error(String.format("Error at reading file: %s", e.getMessage()));
        }
        return "";
    }

    private String buildVerificationUrl(String token) {
        var baseUrl = "http://localhost:8080";
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("/api/v1/email/verify/" + token)
                .toUriString();
    }
}
