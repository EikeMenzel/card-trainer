package com.service.authenticationservice.services;

import com.service.authenticationservice.model.MailType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailQueryServiceTest {

    private EmailQueryService emailQueryService;
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        // Mock the RestTemplate dependency
        restTemplate = Mockito.mock(RestTemplate.class);
        emailQueryService = new EmailQueryService(restTemplate);
    }

    @Test
    void testSendVerificationEmail_Success() {
        Long userId = 123L;
        String expectedUrl = "http://localhost:8081/api/v1/email/verification";
        HttpStatus expectedHttpStatus = HttpStatus.ACCEPTED;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Email sent successfully", HttpStatus.ACCEPTED);
        Mockito.when(restTemplate.postForEntity(expectedUrl, new HttpEntity<>(userId, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode result = emailQueryService.sendEmail(userId, MailType.VERIFICATION);

        assertEquals(expectedHttpStatus, result);
    }

    @Test
    void testSendVerificationEmail_Failure() {
        Long userId = 456L;
        String expectedUrl = "http://localhost:8081/api/v1/email/verification";
        HttpStatus expectedHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("Error sending email", HttpStatus.INTERNAL_SERVER_ERROR);
        Mockito.when(restTemplate.postForEntity(expectedUrl, new HttpEntity<>(userId, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode result = emailQueryService.sendEmail(userId, MailType.VERIFICATION);

        assertEquals(expectedHttpStatus, result);
    }
}
