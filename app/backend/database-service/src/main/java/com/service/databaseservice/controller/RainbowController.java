package com.service.databaseservice.controller;

import com.service.databaseservice.model.Rainbow;
import com.service.databaseservice.payload.out.RainbowListDTO;
import com.service.databaseservice.repository.RainbowRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Retrieve All Blacklisted Passwords",
            description = "Fetches a list of all blacklisted passwords.<br><br>" +
                    "<strong>Note:</strong> This endpoint retrieves passwords stored in a 'rainbow' table, which is typically used for storing compromised or weak passwords." +
                    "<br><br><strong>⚠️ Warning:</strong> This route might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of blacklisted passwords successfully retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RainbowListDTO.class))),
                    @ApiResponse(responseCode = "500", description = "Service could not be reached")
            })
    public ResponseEntity<RainbowListDTO> getAllBlacklistedPasswords() {
        Set<String> rainbowSet = rainbowRepository.findAll().stream().map(Rainbow::getPassword).collect(Collectors.toSet());
        return ResponseEntity.ok(new RainbowListDTO(rainbowSet));
    }
}
