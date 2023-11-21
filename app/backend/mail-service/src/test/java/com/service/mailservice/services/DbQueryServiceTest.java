package com.service.mailservice.services;

import com.service.mailservice.payload.out.UserTokenDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
class DbQueryServiceTest {

    @Value("${db.api.path}/users")
    private String USER_DB_API_PATH;

    @Value("${db.api.path}/user-token")
    private String USER_TOKEN_DB_API_PATH;
    @MockBean
    private RestTemplate restTemplate;
    @Autowired
    private DbQueryService dbQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUserToken_Success() {
        UserTokenDTO userTokenDTO = new UserTokenDTO("token123", Timestamp.from(Instant.now().plus(Duration.ofHours(24L))), "VERIFICATION", 1L);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = new ResponseEntity<>("", HttpStatus.CREATED);

        when(restTemplate.postForEntity(USER_TOKEN_DB_API_PATH + "/", new HttpEntity<>(userTokenDTO, headers), String.class))
                .thenReturn(responseEntity);

        HttpStatusCode statusCode = dbQueryService.saveUserToken(userTokenDTO);

        assertEquals(HttpStatus.CREATED, statusCode);
    }

    @Test
    void getUserEmailFromId_Success() {
        Long userId = 1L;
        String userEmail = "user@example.com";

        ResponseEntity<String> responseEntity = new ResponseEntity<>(userEmail, HttpStatus.OK);

        when(restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertTrue(result.isPresent());
        assertEquals(userEmail, result.get());
    }

    @Test
    void getUserEmailFromId_UserNotFound() {
        Long userId = 1L;

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);

        when(restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/email", String.class))
                .thenReturn(responseEntity);

        Optional<String> result = dbQueryService.getUserEmailFromId(userId);

        assertFalse(result.isPresent());
    }
}

