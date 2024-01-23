package com.service.userservice.services;
import com.service.userservice.payload.inc.TutorialPage;
import org.apache.commons.lang3.tuple.Pair;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.userservice.payload.inc.UserAccountInformationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class DbQueryService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String userDbApiPath;
    private final String achievementUserDbApiPath;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(RestTemplate restTemplate, ObjectMapper objectMapper, @Value("${db.api.path}") String dbPath) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.userDbApiPath = dbPath + "/users";
        this.achievementUserDbApiPath = dbPath + "/user-achievements";
    }

    public Optional<UserAccountInformationDTO> getAccountInformation(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + "/account", String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), UserAccountInformationDTO.class))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public List<Long> getAchievementIds(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(achievementUserDbApiPath + "/users/" + userId + "/achievements/ids", String.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK)
                return objectMapper.readValue(responseEntity.getBody(), new TypeReference<>() {});
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return List.of();
        }
        return List.of();
    }

    public Pair<Optional<UserAccountInformationDTO>, HttpStatusCode> updateAccountInformation(Long userId, UserAccountInformationDTO userAccountInformationDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<UserAccountInformationDTO> requestEntity = new HttpEntity<>(userAccountInformationDTO, headers);

            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    userDbApiPath + "/" + userId + "/account",
                    HttpMethod.PUT,
                    requestEntity,
                    String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                UserAccountInformationDTO responseBody = objectMapper.readValue(responseEntity.getBody(), UserAccountInformationDTO.class);
                return Pair.of(Optional.of(responseBody), HttpStatus.OK);
            }
            return Pair.of(Optional.empty(), responseEntity.getStatusCode());
        } catch (HttpClientErrorException e) {
            logger.debug(e.getMessage());
            return Pair.of(Optional.empty(), e.getStatusCode());
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Pair.of(Optional.empty(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public HttpStatusCode saveTutorialPage(Long userId, TutorialPage tutorialPage) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    userDbApiPath + "/" + userId + "/tutorials/" + tutorialPage.name(),
                    new HttpEntity<>(headers),
                    String.class);
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public boolean doesUserHaveTutorialPage(Long userId, TutorialPage tutorialPage) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(
                    userDbApiPath + "/" + userId + "/tutorials/" + tutorialPage.name(),
                    String.class);

            return responseEntity.getStatusCode() == HttpStatus.NO_CONTENT;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return false;
        }
    }
}
