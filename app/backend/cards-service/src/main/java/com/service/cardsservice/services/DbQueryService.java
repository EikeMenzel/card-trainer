package com.service.cardsservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.cardsservice.payload.Views;
import com.service.cardsservice.payload.in.*;
import com.service.cardsservice.payload.in.export.ExportDTO;
import com.service.cardsservice.payload.out.EmailRequestDTO;
import com.service.cardsservice.payload.out.ImageDataDTO;
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
    private final String dbApiBasePath;
    private final String userDbApiPath;
    private final String emailApiPath;
    private final Logger logger = LoggerFactory.getLogger(DbQueryService.class);
    private final String decksPath;
    private final String cardsPath;
    public DbQueryService(ObjectMapper objectMapper, RestTemplate restTemplate, @Value("${db.api.path}") String dbPath, @Value("${email-service.api.path}") String emailServicePath, @Value("${decks.path}") String decksPath, @Value("${cards.path}") String cardsPath) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
        this.dbApiBasePath = dbPath;
        this.userDbApiPath = this.dbApiBasePath + "/users";
        this.emailApiPath = emailServicePath;
        this.decksPath = decksPath;
        this.cardsPath = cardsPath;
    }

    public Integer getCardsAmountByDeck(Long userId, Long deckId) {
        try {
            String url = userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath + "/count";
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
            String url = userDbApiPath + "/" + userId + decksPath + "/" + deckId + "/cards-to-learn/count";
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
            String url = userDbApiPath + "/" + userId + "/learn-sessions/" + deckId + "/timestamp";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional
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
            String url = userDbApiPath + "/" + userId + decksPath + "/" + deckId + "/learn-state";
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<Integer>>() {
            })
                    : List.of();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return List.of();
        }
    }

    public List<DeckDTO> getAllDecksByUserId(Long userId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath, String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<DeckDTO>>() {
            })
                    : List.of();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return List.of();
        }
    }

    public Optional<DeckDTO> getDeckByUserIdAndDeckId(Long userId, Long deckId) {
        try {
            String url = userDbApiPath + "/" + userId + decksPath + "/" + deckId;
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
                userDbApiPath + "/" + userId + decksPath + "/",
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
                    userDbApiPath + "/" + userId + decksPath + "/" + deckId,
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
                    userDbApiPath + "/" + userId + decksPath + "/" + deckId,
                    HttpMethod.PUT,
                    new HttpEntity<>(deckNameDTO, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) { // Necessary if status-code 409 or 400 is returned.
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public Optional<ExportDTO> getCardExportDTOsForExportDeck(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath + "/export", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Optional.of(objectMapper.readValue(responseEntity.getBody(), new TypeReference<ExportDTO>() {
            }))
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public List<HistoryDTO> getAllHistoriesByUserIdAndDeckId(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + "/histories", String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<HistoryDTO>>() {
            })
                    : List.of();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return List.of();
        }
    }

    public Optional<HistoryDetailDTO> getDetailsHistoryByUserIdAndDeckIdAndHistoryId(Long userId, Long deckId, Long historyId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + "/histories/" + historyId, String.class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? objectMapper.readValue(responseEntity.getBody(), new TypeReference<Optional<HistoryDetailDTO>>() {
            })
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public Pair<List<CardDTO>, HttpStatusCode> getAllCardsByDeckIdAndUserId(Long userId, Long deckId) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath, String.class);
            return responseEntity.getStatusCode() == HttpStatus.OK
                    ? Pair.of(objectMapper.readValue(responseEntity.getBody(), new TypeReference<List<CardDTO>>() {
            }), responseEntity.getStatusCode())
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
                    userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath + "/" + cardId,
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
                userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath,
                new HttpEntity<>(cardNode, headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public HttpStatusCode importDeck(Long userId, ExportDTO exportDTO) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    userDbApiPath + "/" + userId + decksPath + "/import",
                    new HttpEntity<>(objectMapper.writerWithView(Views.Database.class).writeValueAsString(exportDTO), headers),
                    String.class);

            return responseEntity.getStatusCode();
        } catch (JsonProcessingException | HttpServerErrorException exception) {
            logger.debug(exception.getMessage());
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    public HttpStatusCode sendShareDeckEmail(Long senderId, String email, Long deckId) {
        Optional<Long> userId = getUserIdByEmail(email);
        if (userId.isEmpty())
            return ResponseEntity.notFound().build().getStatusCode();

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userId", String.valueOf(senderId));

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                emailApiPath + "/SHARE_DECK",
                new HttpEntity<>(new EmailRequestDTO(userId.get(), deckId), headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<Long> getUserIdByEmail(String email) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/emails/" + email + "/id", String.class);
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
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + "/exists", String.class);
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
                dbApiBasePath + decksPath + "/share/" + token,
                new HttpEntity<>(headers),
                String.class);
        return responseEntity.getStatusCode();
    }

    public Optional<Object> getCardDetails(Long userId, Long deckId, Long cardID) {
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath + "/" + cardID, String.class);
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
            String url = userDbApiPath + "/" + userId + "/images/" + imageId;
            ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
            return (responseEntity.getStatusCode() == HttpStatus.OK)
                    ? Optional.ofNullable(responseEntity.getBody())
                    : Optional.empty();
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public HttpStatusCode updateCard(Long userId, Long deckId, Long cardId, Object cardNode) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    userDbApiPath + "/" + userId + decksPath + "/" + deckId + cardsPath + "/" + cardId,
                    HttpMethod.PUT,
                    new HttpEntity<>(cardNode, headers),
                    Void.class
            );
            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }

    public Optional<Long> saveImage(Long userId, byte[] imageData) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    dbApiBasePath + "/users/" + userId + "/images",
                    new HttpEntity<>(new ImageDataDTO(imageData), headers),
                    String.class);

            return responseEntity.getStatusCode() == HttpStatus.CREATED
                    ? Optional.of(Long.valueOf(Objects.requireNonNull(responseEntity.getBody())))
                    : Optional.empty();
        } catch (HttpServerErrorException e) {
            logger.debug(e.getMessage());
            return Optional.empty();
        }
    }

    public HttpStatusCode saveUserLogin(Long userId) {
        try {
            var headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                    userDbApiPath + "/" + userId + "/user-login-tracker",
                    new HttpEntity<>(headers),
                    String.class);

            return responseEntity.getStatusCode();
        } catch (HttpClientErrorException | HttpServerErrorException e) { //This is not an error! It only means that an entry was already created today
            logger.debug(e.getMessage());
            return e.getStatusCode();
        }
    }
}
