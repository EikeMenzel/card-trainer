package com.service.authenticationservice.services;

import com.service.authenticationservice.model.MailType;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailQueryService {
    private final RestTemplate restTemplate;
    private static final String EMAIL_API_PATH = "http://localhost:8081/api/v1/email";

    public EmailQueryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
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
        switch (mailType) {
            case VERIFICATION -> {
                return sendVerificationEmail(userId);
            }
        }
        return HttpStatusCode.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
