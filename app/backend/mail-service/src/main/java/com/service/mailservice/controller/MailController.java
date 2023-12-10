package com.service.mailservice.controller;

import com.service.mailservice.model.MailType;
import com.service.mailservice.payload.inc.EmailRequestDTO;
import com.service.mailservice.services.MailService;
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
    public ResponseEntity<?> sendEmail(@PathVariable String mailType, @RequestBody EmailRequestDTO emailRequestDTO) {
        Optional<MailType> optionalMailType = MailType.fromString(mailType);
        if(optionalMailType.isEmpty()) {
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
                mailService.sendShareDeckMail(emailRequestDTO.userId(), emailRequestDTO.deckId());
            }
            default -> {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.accepted().build();
    }
}
