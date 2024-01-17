package com.service.mailservice.services;

import com.service.mailservice.payload.inc.UserDailyReminderDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class DailyLearnReminderServiceTest {
    @Mock
    private DbQueryService dbQueryService;

    @Mock
    private MailService mailService;

    @Test
    void testDailyLearnEmail() {
        List<UserDailyReminderDTO> userReminderList = Arrays.asList(
                new UserDailyReminderDTO("user1", "user1@example.com"),
                new UserDailyReminderDTO("user2", "user2@example.com")
        );

        Mockito.when(dbQueryService.getAllEmailsForDailyLearn()).thenReturn(Optional.of(userReminderList));

        DailyLearnReminderService reminderService = new DailyLearnReminderService(dbQueryService, mailService);
        ReflectionTestUtils.setField(reminderService, "dbQueryService", dbQueryService);
        ReflectionTestUtils.setField(reminderService, "mailService", mailService);

        reminderService.dailyLearnEmail();

        Mockito.verify(mailService, Mockito.times(2)).sendDailyLearnReminderMail(Mockito.anyString(), Mockito.anyString());
    }
}
