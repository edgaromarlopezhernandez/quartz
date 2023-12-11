package dev.schedule.Scheduler.controllers;

import dev.schedule.Scheduler.service.AmazonJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manual-job")
@Slf4j
public class ManualJobController {

    @Autowired
    private AmazonJob amazonJob;

    @GetMapping
    public ResponseEntity<String> manualJob() throws JobExecutionException {
        amazonJob.execute(null);
        return ResponseEntity.ok("Job executed");
    }
}
