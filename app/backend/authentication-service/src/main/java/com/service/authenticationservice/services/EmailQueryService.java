package com.service.authenticationservice.services;

import com.service.authenticationservice.model.MailType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailQueryService {
    private final RestTemplate restTemplate;
    private final String EMAIL_API_PATH;

    public EmailQueryService(RestTemplate restTemplate, @Value("${email-service.api.path}") String emailServicePath) {
        this.restTemplate = restTemplate;
        this.EMAIL_API_PATH = emailServicePath;
    }

    private HttpStatusCode sendVerificationEmail(Long userId) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                EMAIL_API_PATH + "/verification",
                new HttpEntity<>(userId, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode sendEmail(Long userId, MailType mailType) {
        return switch (mailType) {
            case VERIFICATION -> sendVerificationEmail(userId);
            case PASSWORD_RESET -> HttpStatus.UNPROCESSABLE_ENTITY;
        };
    }
}