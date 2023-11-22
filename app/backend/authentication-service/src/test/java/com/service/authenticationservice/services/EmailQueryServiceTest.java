package com.service.authenticationservice.services;

import com.service.authenticationservice.model.MailType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class EmailQueryServiceTest {
    @Autowired
    private EmailQueryService emailQueryService;
    @MockBean
    private RestTemplate restTemplate;

    @Value("${email-service.api.path}")
    private String EMAIL_SERVICE_API_PATH;
    @Test
    void testSendVerificationEmail_Success() {
        Long userId = 123L;
        String expectedUrl = EMAIL_SERVICE_API_PATH + "/verification";
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
        String expectedUrl = EMAIL_SERVICE_API_PATH + "/verification";
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
