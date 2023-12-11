package dev.schedule.Scheduler.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Slf4j
@Service
public class AmazonJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.debug("CreateQuartzJob is running......");
        if(jobExecutionContext == null) {
            log.info("AMAZON JOB EXECUTED MANUALLY AT " + LocalDateTime.now());
            System.out.println(MessageFormat.format("Job: {0};\nParam: {1};\nThread: {2}",
                    getClass(), "Manual Job executed", Thread.currentThread().getName()));
        } else {
            log.info("AMAZON JOB EXECUTED BY QUARTZ");
            JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
            String param = dataMap.getString("Marketplace");
            System.out.println(MessageFormat.format("Job: {0};\nParam: {1};\nThread: {2}",
                    getClass(), param, Thread.currentThread().getName()));
        }
    }
}
