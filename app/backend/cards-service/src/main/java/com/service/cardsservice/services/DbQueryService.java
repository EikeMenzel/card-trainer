package com.service.cardsservice.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.in.DeckDTO;
import com.service.cardsservice.payload.in.DeckNameDTO;
import com.service.cardsservice.payload.in.HistoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DbQueryService {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final String DB_API_BASE_PATH;
    private final String USER_DB_API_PATH;
    private final String DECK_DB_API_PATH;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.DB_API_BASE_PATH = dbPath;
        this.USER_DB_API_PATH = this.DB_API_BASE_PATH + "/users";
        this.DECK_DB_API_PATH = this.DB_API_BASE_PATH + "/decks";
    }

    public Integer getCardsAmountByDeck(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards/count";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.parseInt(Objects.requireNonNull(responseEntity.getBody()))
                    : 0;
        } catch (Exception e) {
            logger.info(e.getMessage());
            return 0;
        }
    }

    public Integer getCardsToLearnAmountByDeck(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards-to-learn/count";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.parseInt(Objects.requireNonNull(responseEntity.getBody()))
                    : 0;
        } catch (Exception e) {
            logger.info(e.getMessage());

            return 0;
        }
    }

    public Optional<Timestamp> getLastLearnedTimestampByDeck(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/learn-sessions/" + deckId + "/timestamp";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ?  Optional
                        .ofNullable(responseEntity.getBody())
                        .map(body -> body.replace("\"", ""))
                        .map(OffsetDateTime::parse)
                        .map(odt -> Timestamp.from(odt.toInstant()))
                    : Optional.empty();
        } catch (Exception e) {
            logger.info(e.getMessage());

            return Optional.empty();
        }
    }

    public List<Integer> getLearnStateOfDeck(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/learn-state";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<Integer>>(){})
                    : List.of();
        } catch (Exception e) {
            logger.info(e.getMessage());

            return List.of();
        }
    }

    public List<DeckDTO> getAllDecksByUserId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/decks", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<DeckDTO>>(){})
                    : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
    public Optional<DeckDTO> getDeckByUserIdAndDeckId(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/decks/" + deckId;
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.ofNullable(objectMapper.readValue(responseEntity.getBody(), DeckDTO.class))
                    : Optional.empty();
        } catch (Exception e) {
            logger.info(e.getMessage());

            return Optional.empty();
        }
    }
    public HttpStatusCode saveDeck(Long userId, DeckNameDTO deckNameDTO) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                USER_DB_API_PATH + "/" + userId + "/decks/",
                new HttpEntity<>(deckNameDTO, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode deleteDeck(Long userId, Long deckId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    USER_DB_API_PATH + "/" + userId + "/decks/" + deckId,
                    HttpMethod.DELETE,
                    entity,
                    String.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            return e.getStatusCode();
        }
    }

    public HttpStatusCode updateDeckInformation(Long userId, Long deckId, DeckNameDTO deckNameDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    USER_DB_API_PATH + "/" + userId + "/decks/" + deckId,
                    HttpMethod.PUT,
                    new HttpEntity<>(deckNameDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) { // Necessary if status-code 409 or 400 is returned.
            return e.getStatusCode();
        }
    }

    public List<HistoryDTO> getAllHistoriesByUserId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/histories", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<HistoryDTO>>(){})
                    : List.of();
        } catch (Exception e) {
            return List.of();
        }
    }
}
