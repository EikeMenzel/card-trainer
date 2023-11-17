package com.service.databaseservice.controller;

import com.service.databaseservice.model.Rainbow;
import com.service.databaseservice.payload.out.RainbowListDTO;
import com.service.databaseservice.repository.RainbowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/db")
public class RainbowController {
    private final RainbowRepository rainbowRepository;
    public RainbowController(RainbowRepository rainbowRepository) {
        this.rainbowRepository = rainbowRepository;
    }

    @GetMapping("/rainbows")
    public ResponseEntity<RainbowListDTO> getAllBlacklistedPasswords() {
        Set<String> rainbowSet = rainbowRepository.findAll().stream().map(Rainbow::getPassword).collect(Collectors.toSet());
        return ResponseEntity.ok(new RainbowListDTO(rainbowSet));
    }
}
