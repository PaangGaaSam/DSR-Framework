package org.github.dsropensource.service;

import org.quartz.Job;

import java.util.Map;

public interface QuartzService {

    void addJob(String jobName, Class<? extends Job> cls, String time, Map<String, Object> parameter);

    void addJob(String jobName, Class<? extends Job> cls, String time);

    void modifyJobTime(String jobName, String time);

    void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName);

    void removeJob(String jobName);

    void startJobs();

    void shutdownJobs();
}
