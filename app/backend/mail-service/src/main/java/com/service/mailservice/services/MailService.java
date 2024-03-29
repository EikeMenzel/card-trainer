package com.service.mailservice.services;

import com.service.mailservice.model.MailType;
import com.service.mailservice.payload.inc.UserAccountInformationDTO;
import com.service.mailservice.payload.out.UserTokenDTO;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class MailService {
    private final Logger logger = LoggerFactory.getLogger(MailService.class);
    private final JavaMailSender mailSender;
    private final DbQueryService dbQueryService;
    private final MailContentBuilder mailContentBuilder;

    @Value("${mail.username}")
    private String sendFrom;

    public MailService(JavaMailSender mailSender, DbQueryService dbQueryService, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.dbQueryService = dbQueryService;
        this.mailContentBuilder = mailContentBuilder;
    }

    private void sendHtmlMail(String to, String subject, String content) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true, "UTF-8");

            message.setSubject(subject);
            helper.setTo(to);
            helper.setFrom(sendFrom);

            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException messagingException) {
            logger.error(messagingException.getMessage());
        }
    }

    public void sendVerificationMail(Long userId) {
        Optional<String> userEmail = dbQueryService.getUserEmailFromId(userId);
        if (userEmail.isEmpty()) {
            logger.error("user-email from id not found");
            return;
        }
        Optional<UserAccountInformationDTO> userAccountInformationDTOOptional = dbQueryService.getAccountInformation(userId);
        userAccountInformationDTOOptional.ifPresent(userAccountInformationDTO -> {
            String token = TokenService.generateToken();
            String content = mailContentBuilder.getContent(MailType.VERIFICATION, userAccountInformationDTO.langCode(), token, userAccountInformationDTO.username());

            var httpStatusCode = dbQueryService.saveUserToken(new UserTokenDTO(token, Timestamp.from(Instant.now().plus(Duration.ofHours(24))), MailType.VERIFICATION.toString(), userId));
            if (httpStatusCode == HttpStatus.CREATED) {
                sendHtmlMail(userEmail.get(), "Verification-Mail", content);
            } else {
                logger.error("can't create user-token in database");
            }
        });
    }

    public void sendPasswordResetMail(Long userId) {
        Optional<UserAccountInformationDTO> userAccountInformationDTOOptional = dbQueryService.getAccountInformation(userId);
        userAccountInformationDTOOptional.ifPresent(userAccountInformation -> {
            String token = TokenService.generateToken();
            String content = mailContentBuilder.getContent(MailType.PASSWORD_RESET, userAccountInformation.langCode(), token, userAccountInformation.email(), userAccountInformation.username());

            var httpStatusCode = dbQueryService.saveUserToken(new UserTokenDTO(token, Timestamp.from(Instant.now().plus(Duration.ofHours(24))), MailType.PASSWORD_RESET.toString(), userId));
            if (httpStatusCode == HttpStatus.CREATED) {
                sendHtmlMail(userAccountInformation.email(), "Password-Reset-Mail", content);
            }
        });
    }
    public void sendShareDeckMail(Long receiverId, Long senderId, Long deckId) {
        Optional<String> senderUsername = dbQueryService.getAccountInformation(senderId)
                .map(UserAccountInformationDTO::username);

        if(senderUsername.isEmpty())
            return;

        Optional<UserAccountInformationDTO> userAccountInformationDTOOptional = dbQueryService.getAccountInformation(receiverId);
        userAccountInformationDTOOptional.ifPresent(userAccountInformationDTO -> {
            Optional<String> optionalDeckDTO = dbQueryService.getDeckNameByDeckId(deckId);
            optionalDeckDTO.ifPresent(deckName -> {
                String token = TokenService.generateShareDeckToken(deckId);
                String content = mailContentBuilder.getContent(MailType.SHARE_DECK, userAccountInformationDTO.langCode(), token, deckName, senderUsername.get(), userAccountInformationDTO.username());

                var httpStatusCode = dbQueryService.saveUserToken(new UserTokenDTO(token, Timestamp.from(Instant.now().plus(Duration.ofDays(7))), MailType.SHARE_DECK.toString(), receiverId));
                if (httpStatusCode == HttpStatus.CREATED) {
                    sendHtmlMail(userAccountInformationDTO.email(), "Share-Deck-Mail", content);
                }
            });
        });
    }
    public void sendDailyLearnReminderMail(String username, String language, String email) {
        String content = mailContentBuilder.getContent(MailType.DAILY_REMINDER, language, username);
        sendHtmlMail(email, "DailyLearnReminder", content);
    }
}

