package com.oppalove.util;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.oppalove.util.OpStopWatch.convertTimeUnit;

/**
 * This class creates summarize string.
 */
public class DefaultSummary implements Summary {

    private static String BAR = "===========================";

    @Override
    public String summaryString(OpStopWatch stopWatch, TimeUnit timeUnit) {

        StringBuffer buffer = new StringBuffer("\n")
                .append(Optional.ofNullable(stopWatch.getName()).orElse(""))
                .append(" (")
                .append(timeUnit.name())
                .append(")")
                .append("\n")
                .append(BAR)
                .append("\n");

        stopWatch.getTasks().stream()
                .forEach(task -> {
                    buffer.append(Optional.ofNullable(task.getName()).orElse(""))
                            .append("\t\t")
                            .append(convertTimeUnit(task.getElapsedTime(), TimeUnit.NANOSECONDS, timeUnit))
                            .append("\n");
                });

        buffer.append("Total : ")
                .append(stopWatch.totalTime(timeUnit))
                .append(", ")
                .append("Average : ")
                .append(stopWatch.average(timeUnit))
                .append(", ")
                .append("Min : ")
                .append(stopWatch.min(timeUnit))
                .append(", ")
                .append("Max : ")
                .append(stopWatch.max(timeUnit));


        return buffer.toString();
    }
}
