package org.github.dsropensource.config;

import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

@Configuration
public class QuartzManager {

    private static final String JOB_GROUP_NAME = "FH_JOBGROUP_NAME"; // 任务组
    private static final String TRIGGER_GROUP_NAME = "FH_TRIGGERGROUP_NAME"; // 触发器组

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public JobFactory jobFactory()
    {
        AutowiredSpringBeanJobFactory jobFactory = new AutowiredSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() throws IOException
    {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setQuartzProperties(quartzProperties());
        schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
        schedulerFactory.setAutoStartup(true);
        schedulerFactory.setJobFactory(jobFactory());

        return schedulerFactory;
    }

    public String getJobGroupName()
    {
        return JOB_GROUP_NAME;
    }

    public String getTriggerGroupName()
    {
        return TRIGGER_GROUP_NAME;
    }

    public Properties quartzProperties() throws IOException
    {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();

        return propertiesFactoryBean.getObject();
    }
}
