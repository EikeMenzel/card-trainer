package com.service.cardsservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.Views;
import com.service.cardsservice.payload.in.*;
import com.service.cardsservice.payload.in.export.ExportDTO;
import com.service.cardsservice.payload.out.EmailRequestDTO;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
    private final String EMAIL_API_PATH;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);

    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath, @Value("${email-service.api.path}") String emailServicePath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.DB_API_BASE_PATH = dbPath;
        this.USER_DB_API_PATH = this.DB_API_BASE_PATH + "/users";
        this.EMAIL_API_PATH = emailServicePath;
    }

    public Integer getCardsAmountByDeck(Long userId, Long deckId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards/count";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Integer.parseInt(Objects.requireNonNull(responseEntity.getBody()))
                    : 0;
        } catch (Exception e) {
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
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
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public Optional<ExportDTO> getCardExportDTOsForExportDeck(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards/export", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), new TypeReference<ExportDTO>(){}))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public List<HistoryDTO> getAllHistoriesByUserId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/histories", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<HistoryDTO>>(){})
                    : List.of();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return List.of();
        }
    }

    public Optional<HistoryDetailDTO> getDetailsHistoryByUserIdAndHistoryId(Long userId, Long historyId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/histories/" + historyId, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
            ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<Optional<HistoryDetailDTO>>() {})
            : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Pair<List<CardDTO>, HttpStatusCode> getAllCardsByDeckIdAndUserId(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Pair.of(objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CardDTO>>(){}), responseEntity.getStatusCode())
                    : Pair.of(List.of(), responseEntity.getStatusCode());
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Pair.of(List.of(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public HttpStatusCode deleteCard(Long userId, Long deckId, Long cardId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            return restTemplate.exchange(
                    USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards/" + cardId,
                    HttpMethod.DELETE,
                    entity,
                    String.class).getStatusCode();
        } catch (HttpClientErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public HttpStatusCode saveCardByDeckIdAndUserId(Long userId, Long deckId, Object cardNode) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards",
                new HttpEntity<>(cardNode, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode importDeck(Long userId, ExportDTO exportDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    USER_DB_API_PATH + "/" + userId + "/decks/import",
                    new HttpEntity<>(objectMapper.writerWithView(Views.Database.class).writeValueAsString(exportDTO), headers),
                    String.class);

            return responseEntity.getStatusCode();
        } catch (JsonProcessingException | HttpServerErrorException exception) {
            logger.debug(exception.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public HttpStatusCode sendShareDeckEmail(String email, Long deckId) {
        Optional<Long> userId = getUserIdByEmail(email);
        if(userId.isEmpty())
            return ResponseEntity.notFound().build().getStatusCode();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                EMAIL_API_PATH + "/SHARE_DECK",
                new HttpEntity<>(new EmailRequestDTO(userId.get(), deckId), headers),
                String.class);
        return responseEntity.getStatusCode();
    }
    public Optional<Long> getUserIdByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/emails/" + email + "/id", String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.of(Long.valueOf(Objects.requireNonNull(responseEntity.getBody())))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public HttpStatusCode existsDeckByUserIdAndDeckId(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/exists", String.class);
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public HttpStatusCode shareDeck(String token) {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                DB_API_BASE_PATH + "/decks/share/" + token,
                new HttpEntity<>(headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<Object> getCardDetails(Long userId, Long deckId, Long cardID) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(USER_DB_API_PATH + "/" + userId + "/decks/" + deckId + "/cards/" + cardID, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<byte[]> getImage(Long userId, Long imageId) {
        try {
            String url = USER_DB_API_PATH + "/" + userId + "/images/" + imageId;
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }
}
