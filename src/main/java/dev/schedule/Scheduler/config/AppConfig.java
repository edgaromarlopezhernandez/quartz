package dev.schedule.Scheduler.config;

import dev.schedule.Scheduler.service.AmazonJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Configuration
public class AppConfig implements SchedulingConfigurer {

    @Value("${cron-string:0/5 * * * * ?}")
    private String cronString;

    @Bean
    public SchedulerFactoryBean customSchedulerFactoryBean1(/*DataSource dataSource*/) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        Properties properties = new Properties();
        properties.setProperty("org.quartz.threadPool.threadNamePrefix", "my-custom-scheduler1_Worker");
        factory.setQuartzProperties(properties);
        //factory.setDataSource(dataSource);
        return factory;
    }

    @Bean
    public Scheduler customScheduler(SchedulerFactoryBean factory) throws SchedulerException {
        Scheduler scheduler = factory.getScheduler();
        scheduler.start();
        return scheduler;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(10);
        threadPoolTaskScheduler.setThreadNamePrefix("my-scheduled-task-pool-");
        threadPoolTaskScheduler.initialize();

        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    @Bean
    public CommandLineRunner run(Scheduler customScheduler) {
        return (String[] args) -> {
            Date afterFiveSeconds = Date.from(LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant());

            JobDetail jobDetail1 = JobBuilder.newJob(AmazonJob.class)
                    .usingJobData("Marketplace", "Amazon Vendor Scheduled Job executed at: "  + LocalDateTime.now()).build();
            Trigger trigger1 = TriggerBuilder.newTrigger()
                    .withIdentity("Main trigger", "Amazon vendor group")
                    /*.startAt(afterFiveSeconds).build();*/
                    .withSchedule(cronSchedule(cronString))
                    //.forJob("myJob", "group1")
                    .build();
            customScheduler.scheduleJob(jobDetail1, trigger1);
        };
    }
}
