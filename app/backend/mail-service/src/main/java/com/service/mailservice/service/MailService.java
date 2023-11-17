package com.service.mailservice.service;

import com.service.mailservice.model.MailType;
import com.service.mailservice.payload.out.UserTokenDTO;
import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

    public MailService(JavaMailSender mailSender, DbQueryService dbQueryService, MailContentBuilder mailContentBuilder) {
        this.mailSender = mailSender;
        this.dbQueryService = dbQueryService;
        this.mailContentBuilder = mailContentBuilder;
    }

    private void sendHtmlMail(String to, String subject, String content) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);

            message.setSubject(subject);
            helper.setTo(to);
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

        String token = TokenService.generateToken();
        String content = mailContentBuilder.getContent(MailType.VERIFICATION, token);

        HttpStatusCode httpStatusCode = dbQueryService.saveUserToken(new UserTokenDTO(token, Timestamp.from(Instant.now().plus(Duration.ofHours(24))), MailType.VERIFICATION.toString(), userId));
        if (httpStatusCode == HttpStatus.CREATED) {
            logger.error("Test");
            sendHtmlMail(userEmail.get(), "Verification-Mail", content);
        } else {
            logger.error("can't create user-token in database");
        }
    }
}

