package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {
    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Questo metodo viene eseguito prima dell'avvio del job
        log.info("Job iniziato: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // Questo metodo viene eseguito dopo il completamento del job (indipendentemente dal suo esito)
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Job completato con successo.");
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("Job fallito con errori.");
        }
    }
}
