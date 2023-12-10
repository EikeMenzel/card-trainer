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
    private final String GATEWAY_PATH;

    public MailContentBuilder(@Value("${gateway.api.path}") String gatewayPath) {
        this.GATEWAY_PATH = gatewayPath;
    }
    public String getContent(MailType mailType, String... data) {
        return switch (mailType) {
            case VERIFICATION -> getContentFromFile("static/mail_verification.html")
                    .replace("${verificationUrl}", buildVerificationUrl(data[0]));
            case DAILY_REMINDER -> getContentFromFile("static/daily_learn_reminder.html")
                    .replace("${username}", data[1])
                    .replace("${dailyLearnUrl}", GATEWAY_PATH);
            case PASSWORD_RESET -> getContentFromFile("static/mail_password_reset.html")
                    .replace("${resetUrl}", buildPasswordResetUrl(data[0], data[1]));
            case SHARE_DECK -> getContentFromFile("static/share_deck.html")
                    .replace("${shareDeckUrl}", buildShareDeckUrl(data[0]))
                    .replace("${deckName}", data[1])
                    .replace("${senderName}", data[2]);
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
        return UriComponentsBuilder.fromHttpUrl(GATEWAY_PATH)
                .path("/api/v1/email/verify/" + token)
                .toUriString();
    }

    private String buildPasswordResetUrl(String token, String email) {
        return UriComponentsBuilder.fromHttpUrl(GATEWAY_PATH)
                .path("/reset-password")
                .queryParam("token", token)
                .queryParam("email", email)
                .toUriString();
    }

    private String buildShareDeckUrl(String token) {
        return UriComponentsBuilder.fromHttpUrl(GATEWAY_PATH)
                .path("/api/v1/decks/share/" + token)
                .toUriString();
    }
}
