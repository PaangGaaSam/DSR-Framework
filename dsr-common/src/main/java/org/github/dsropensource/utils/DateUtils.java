package org.github.dsropensource.utils;

import org.quartz.CronExpression;

import java.text.ParseException;

public class DateUtils {

    public static boolean isValid(String cron)
    {
        return CronExpression.isValidExpression(cron);
    }
}
