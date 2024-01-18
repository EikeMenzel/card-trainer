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
    private final String userNamePlaceholder = "${username}";

    public MailContentBuilder(@Value("${gateway.api.path}") String gatewayPath) {
        this.GATEWAY_PATH = gatewayPath;
    }
    public String getContent(MailType mailType, String language, String... data) {
        return switch (mailType) {
            case VERIFICATION ->  MailContentProvider.MAIL_VERIFICATION
                    .replace("${verificationUrl}", buildVerificationUrl(data[0]))
                    .replace(userNamePlaceholder,  data[1]);

            case DAILY_REMINDER -> switch (language.toUpperCase()) {
                case "DE" -> MailContentProvider.DAILY_LEARN_REMINDER_DE
                        .replace(userNamePlaceholder, data[0])
                        .replace("${dailyLearnUrl}", GATEWAY_PATH);
                case "EN" -> MailContentProvider.DAILY_LEARN_REMINDER_EN
                        .replace(userNamePlaceholder, data[0])
                        .replace("${dailyLearnUrl}", GATEWAY_PATH);
                default -> "";
            };
            case PASSWORD_RESET -> switch (language.toUpperCase()) {
                case "DE" -> MailContentProvider.MAIL_PASSWORD_RESET_DE
                        .replace("${resetUrl}", buildPasswordResetUrl(data[0], data[1]))
                        .replace(userNamePlaceholder, data[2]);
                case "EN" -> MailContentProvider.MAIL_PASSWORD_RESET_EN
                        .replace("${resetUrl}", buildPasswordResetUrl(data[0], data[1]))
                        .replace(userNamePlaceholder, data[2]);
                default -> "";
            };
            case SHARE_DECK -> switch (language.toUpperCase()) {
                case "DE" -> MailContentProvider.SHARE_DECK_DE
                        .replace("${shareDeckUrl}", buildShareDeckUrl(data[0]))
                        .replace("${deckName}", data[1])
                        .replace("${senderName}", data[2])
                        .replace("${username}", data[3]);

                case "EN" -> MailContentProvider.SHARE_DECK_EN
                        .replace("${shareDeckUrl}", buildShareDeckUrl(data[0]))
                        .replace("${deckName}", data[1])
                        .replace("${senderName}", data[2])
                        .replace("${username}", data[3]);

                default -> "";
            };
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
