package com.ing.hubs.util;

import com.ing.hubs.service.EnrollmentService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnrollmentScheduler {
    private EnrollmentService enrollmentService;

    @Scheduled(cron = "0 13 0 * * *")
    public void checkAndDenyOldPendingEnrollments() {
        enrollmentService.denyOldPendingEnrollments();
    }
}
