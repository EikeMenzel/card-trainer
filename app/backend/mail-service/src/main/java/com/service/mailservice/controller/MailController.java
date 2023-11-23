package com.service.mailservice.controller;

import com.service.mailservice.model.MailType;
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
    public ResponseEntity<?> sendEmail(@PathVariable String mailType, @RequestBody Long userId) {
        Optional<MailType> optionalMailType = MailType.fromString(mailType);
        if(optionalMailType.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        switch (optionalMailType.get()) {
            case VERIFICATION: {
                mailService.sendVerificationMail(userId);
                return ResponseEntity.accepted().build();
            }
            default: {
                return ResponseEntity.internalServerError().build();
            }
        }
    }

}
