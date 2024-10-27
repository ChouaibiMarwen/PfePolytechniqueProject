package com.smarty.pfeserver.Services.SchedulerServices;


import com.smarty.pfeserver.Enum.Project.MissionStatusEnum;
import com.smarty.pfeserver.Models.Project.Mission;
import com.smarty.pfeserver.Repository.Project.MissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MissionStatusScheduler {

    @Autowired
    private MissionRepository missionRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Runs at midnight every day
    public void updateOverdueMissions() {
        // Calculate "yesterday" as the day before the current midnight
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date yesterday = calendar.getTime();

        // Define the statuses to check
        List<MissionStatusEnum> statuses = Arrays.asList(MissionStatusEnum.PENDING, MissionStatusEnum.IN_PROGRESS);

        // Find missions that are PENDING or IN_PROGRESS and ended exactly yesterday
        List<Mission> missions = missionRepository.findByStatusInAndEnddate(statuses, yesterday);

        // Mark each of these missions as OVERDUE
        for (Mission mission : missions) {
            mission.setStatus(MissionStatusEnum.OVERDUE);
        }

        missionRepository.saveAll(missions);
    }
}