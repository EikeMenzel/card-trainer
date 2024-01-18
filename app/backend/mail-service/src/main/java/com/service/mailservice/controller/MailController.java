package com.service.mailservice.controller;

import com.service.mailservice.model.MailType;
import com.service.mailservice.payload.inc.EmailRequestDTO;
import com.service.mailservice.services.MailService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/v1/email")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/{mailType}")
    @Operation(summary = "Send email based on type",
            description = "Send different types of emails like verification, password reset, or share deck.<br><br>" +
                    "<strong>⚠️ Warning:</strong> This method might not work correctly in Swagger UI due to internal services which are not exposed to the frontend.",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Email accepted for sending"),
                    @ApiResponse(responseCode = "404", description = "Mail type not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> sendEmail(
            @RequestHeader(required = false) Long userId,
            @Parameter(description = "Type of the email to send", required = true) @PathVariable String mailType,
            @Parameter(description = "Email request details", required = true, schema = @Schema(implementation = EmailRequestDTO.class))
            @RequestBody EmailRequestDTO emailRequestDTO) {

        Optional<MailType> optionalMailType = MailType.fromString(mailType);
        if (optionalMailType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        switch (optionalMailType.get()) {
            case VERIFICATION -> {
                mailService.sendVerificationMail(emailRequestDTO.userId());
            }
            case PASSWORD_RESET -> {
                mailService.sendPasswordResetMail(emailRequestDTO.userId());
            }
            case SHARE_DECK -> {
                mailService.sendShareDeckMail(emailRequestDTO.userId(), userId, emailRequestDTO.deckId());
            }
            default -> {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.accepted().build();
    }
}
