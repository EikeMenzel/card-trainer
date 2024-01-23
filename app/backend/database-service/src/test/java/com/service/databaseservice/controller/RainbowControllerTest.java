package com.service.databaseservice.controller;

import com.service.databaseservice.model.Rainbow;
import com.service.databaseservice.payload.out.RainbowListDTO;
import com.service.databaseservice.repository.RainbowRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RainbowControllerTest {

    @Mock
    private RainbowRepository rainbowRepository;

    @InjectMocks
    private RainbowController rainbowController;

    @Test
    void testGetAllBlacklistedPasswords_Success() {
        List<Rainbow> rainbowList = Arrays.asList(new Rainbow("password1"), new Rainbow("password2"));

        when(rainbowRepository.findAll()).thenReturn(rainbowList);
        ResponseEntity<RainbowListDTO> response = rainbowController.getAllBlacklistedPasswords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).rainBowList().size());
    }

    @Test
    void testGetAllBlacklistedPasswords_EmptySet() {
        when(rainbowRepository.findAll()).thenReturn(List.of());

        ResponseEntity<RainbowListDTO> response = rainbowController.getAllBlacklistedPasswords();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).rainBowList().size());
    }
}
