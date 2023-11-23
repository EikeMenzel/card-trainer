package com.service.mailservice.services;

import com.service.mailservice.model.MailType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;

@Service
public class MailContentBuilder {
    private final Logger logger =  LoggerFactory.getLogger(MailContentBuilder.class);
    private final String AUTH_API_BASE_PATH;

    public MailContentBuilder(@Value("${auth-service.api.path}") String authServiceApiPath) {
        this.AUTH_API_BASE_PATH = authServiceApiPath;
    }
    public String getContent(MailType mailType, String data) {
        return switch (mailType) {
            case VERIFICATION -> getContentFromFile("static/mail_verification.html").replace("${verificationUrl}", buildVerificationUrl(data));
            case DAILY_REMINDER -> getContentFromFile("static/daily-learn-reminder.html").replace("${username}", data).replace("${dailyLearnUrl}", "http://localhost:4200");
        };
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
        return UriComponentsBuilder.fromHttpUrl(AUTH_API_BASE_PATH)
                .path("/api/v1/email/verify/" + token)
                .toUriString();
    }
}
