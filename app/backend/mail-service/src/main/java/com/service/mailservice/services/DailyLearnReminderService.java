package com.service.mailservice.services;

import com.service.mailservice.payload.inc.UserDailyReminderDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DailyLearnReminderService {
    private final DbQueryService dbQueryService;
    private final MailService mailService;
    public DailyLearnReminderService(DbQueryService dbQueryService, MailService mailService) {
        this.dbQueryService = dbQueryService;
        this.mailService = mailService;
    }

    @Scheduled(cron = "0 0 16 * * ?")
    public void dailyLearnEmail() {
        Optional<List<UserDailyReminderDTO>> userDailyReminderDTOList = dbQueryService.getAllEmailsForDailyLearn();
        userDailyReminderDTOList.ifPresent(
                list -> list.forEach(
                        entry -> mailService.sendDailyLearnReminderMail(entry.username(), entry.email()
                        )
                )
        );
    }
}
