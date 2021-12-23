package org.github.dsropensource.service.impl;

import org.github.dsropensource.config.QuartzManager;
import org.github.dsropensource.service.QuartzService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class QuartzServiceImpl implements QuartzService {

    @Autowired
    private QuartzManager quartzManager;

    @Override
    public void addJob(String jobName, Class<? extends Job> cls, String time, Map<String, Object> parameter) {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler(); // 通过SchedulerFactory构建Scheduler对象
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, quartzManager.getJobGroupName()).build(); // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
            jobDetail.getJobDataMap().put("parameterList", parameter); // 传参数
            CronTrigger trigger = TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
                    .withIdentity(jobName, quartzManager.getTriggerGroupName()) // 给触发器起一个名字和组名
                    .withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {
                sched.start(); // 启动
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addJob(String jobName, Class<? extends Job> cls, String time) {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler(); // 通过SchedulerFactory构建Scheduler对象
            JobDetail jobDetail = JobBuilder.newJob(cls).withIdentity(jobName, quartzManager.getJobGroupName()).build(); // 用于描叙Job实现类及其他的一些静态信息，构建一个作业实例
            CronTrigger trigger = TriggerBuilder.newTrigger() // 创建一个新的TriggerBuilder来规范一个触发器
                    .withIdentity(jobName, quartzManager.getJobGroupName()) // 给触发器起一个名字和组名
                    .withSchedule(CronScheduleBuilder.cronSchedule(time)).build();
            sched.scheduleJob(jobDetail, trigger);
            if (!sched.isShutdown()) {
                sched.start(); // 启动
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modifyJobTime(String jobName, String time) {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler(); // 通过SchedulerFactory构建Scheduler对象
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, quartzManager.getTriggerGroupName()); // 通过触发器名和组名获取TriggerKey
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey); // 通过TriggerKey获取CronTrigger
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobKey jobKey = JobKey.jobKey(jobName, quartzManager.getJobGroupName()); // 通过任务名和组名获取JobKey
                JobDetail jobDetail = sched.getJobDetail(jobKey);
                Class<? extends Job> objJobClass = jobDetail.getJobClass();
                removeJob(jobName);
                addJob(jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName); // 通过触发器名和组名获取TriggerKey
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName); // 通过任务名和组名获取JobKey
            sched.pauseTrigger(triggerKey); // 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(jobKey); // 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeJob(String jobName) {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(jobName, quartzManager.getJobGroupName()); // 通过触发器名和组名获取TriggerKey
            JobKey jobKey = JobKey.jobKey(jobName, quartzManager.getJobGroupName()); // 通过任务名和组名获取JobKey
            sched.pauseTrigger(triggerKey); // 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(jobKey); // 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void startJobs() {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler();
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void shutdownJobs() {
        try {
            Scheduler sched = quartzManager.schedulerFactoryBean().getScheduler();
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
